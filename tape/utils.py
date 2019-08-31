import os
from pydub import AudioSegment

def delete_all_data_files():
    '''
    프로그램을 시작하기 전 프로그램의 temp 데이터를 삭제합니다.
    '''

    list_detector_result = os.listdir('detector_result/')
    list_extract_audio = os.listdir('extract_audio/')
    list_extract_audio_0 = os.listdir('extract_audio/output0/')
    list_extract_audio_1 = os.listdir('extract_audio/output1/')

    list_detector_result = [var for var in list_detector_result if var.endswith('txt')]
    list_extract_audio = [var for var in list_extract_audio if var.endswith('wav')]
    list_extract_audio_0 = [var for var in list_extract_audio_0 if var.endswith('wav')]
    list_extract_audio_1 = [var for var in list_extract_audio_1 if var.endswith('wav')]

    for val in list_detector_result:
        os.remove('detector_result/' + val)

    for val in list_extract_audio:
        os.remove('extract_audio/' + val)

    for val in list_extract_audio_0:
        os.remove('extract_audio/output0/' + val)

    for val in list_extract_audio_1:
        os.remove('extract_audio/output1/' + val)

def extract_audio_in_video(input_video_path):
    '''
    입력된 비디오에서 오디오를 추출합니다.
    :param input_video_path: 입력할 비디오의 경로입니다.
    :return:
    '''
    return os.system('ffmpeg -y -i ' + input_video_path + ' -vn -ar 44.1k -ac 1 -ab 256k extract_audio/all_extract_audio.wav')

def extract_image_in_video(input_video_path):
    '''
    비디오에서 썸네일 이미지를 추출합니다.
    :param input_video_path: 입력할 비디오의 경로입니다.
    :return:
    '''
    return os.system('ffmpeg -ss 00:00:00 -i ' + input_video_path + ' -y -vframes 1 -an -s 1280x720 output/' + input_video_path[input_video_path.find('/') + 1:input_video_path.find('.')] + '.jpg')

def divide_audio(divide_time):
    '''
    오디오 파일을 여러 개의 파일로 나눕니다.
    :param divide_time: 오디오를 몇 초 간격으로 나눌지 설정합니다.
    :return: 자른 오디오를 저장한 경로와 시간을 return 합니다.
    '''
    entire_audio = AudioSegment.from_wav('extract_audio/all_extract_audio.wav')

    entire_audio_len = len(entire_audio) * 0.001
    segment_count = 0
    segment_len = entire_audio_len
    for i in range(2, 100):
        if entire_audio_len / i <= divide_time: #자를 오디오의 길이
            segment_len /= i
            segment_count = i
            break

    path_and_startend_0, path_and_startend_1 = [], []

    #첫번 째 작업
    for i in range(segment_count):
        audio_divided = entire_audio[i * segment_len * 1000:i * segment_len * 1000 + segment_len * 1000]
        path_and_startend_0 += [['extract_audio/output0/output0_' + str(i) + '.wav', i * segment_len, i * segment_len + segment_len]]
        audio_divided.export('extract_audio/output0/output0_' + str(i) + '.wav', format='wav')

    #두번 째 작업
    for i in range(segment_count):
        audio_divided = entire_audio[i * segment_len * 1000 + 5000:i * segment_len * 1000 + segment_len * 1000 + 5000]
        path_and_startend_1 += [['extract_audio/output1/output1_' + str(i) + '.wav', i * segment_len + 5, i * segment_len + segment_len + 5]]
        audio_divided.export('extract_audio/output1/output1_' + str(i) + '.wav', format='wav')

    return path_and_startend_0, path_and_startend_1

def generate_sound(start_and_end_time):
    '''
    욕을 말한 시작점과 끝점을 입력받아 욕을 한 시점에 비프음 소리를 냅니다.
    :param start_and_end_time: 욕을 말한 시작점과 끝점입니다.
    '''
    beep_sound = AudioSegment.from_wav('beep.wav')
    result_sound = AudioSegment.from_wav('extract_audio/all_extract_audio.wav')

    for start, end in start_and_end_time:
        result_sound = result_sound[:start * 1000] + beep_sound[:(end - start) * 1000] + result_sound[end * 1000:]

    result_sound.export('detector_result/audio_result.wav', format='wav')

def combine_audio_and_video(video_path):
    '''
    비디오의 경로를 입력받아 비프음 처리된 소리와 합칩니다.
    :param video_path: 합칠 비디오의 이름을 포함한 경로입니다.
    :return:
    '''
    return os.system('ffmpeg -y -i ' + video_path + ' -i detector_result/audio_result.wav -c:v copy -c:a aac -strict experimental -map 0:v:0 -map 1:a:0 output/' + video_path[video_path.find('/') + 1:video_path.find('.')] + '.mp4')

def get_all_data():
    '''
    detector_result에 생성된 욕 데이터를 불러옵니다.
    :return: 모든 결과를 return 합니다.
    '''
    list_result_detector = os.listdir('detector_result/')

    list_result_detector = [name for name in list_result_detector if name.endswith('txt')]

    all_result = []
    for path in list_result_detector:
        f = open('detector_result/' + path, 'r')
        result = f.read().split()

        for i, val in enumerate(result):
            result[i] = val.split(',')
            result[i][0] = float(result[i][0])
            result[i][1] = float(result[i][1])

        all_result += result

    return all_result
