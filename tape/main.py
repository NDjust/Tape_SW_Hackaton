import utils
import threading
import profane_recognizer

input_file = 'input/test2.mp4'

if __name__ == '__main__':

    #프로그램을 시작하기 전 tmp 데이터들을 삭제합니다.
    print('TEMP 데이터 삭제')
    utils.delete_all_data_files()

    #ffmpeg를 사용하여 입력된 비디오에서 오디오를 추출합니다.
    print('오디오 추출')
    utils.extract_audio_in_video(input_file)

    #ffmpeg를 사용하여 입력된 비디오에서 썸네일을 추출합니다.
    print('썸네일 추출')
    utils.extract_image_in_video(input_file)

    #오디오를 30초의 간격으로 자릅니다.
    print('오디오 자르기')
    audio_data_0, audio_data_1 = utils.divide_audio(30.0)

    '''
    Google speech api를 사용하여 30초의 간격으로 나눠진 사용자의 오디오를 넣어 결과를 저장합니다.
    나눠진 오디오를 차례차례 하나씩 Google speech api를 거치는 것은 작업 속도가 느리기 때문에 threading을 사용하여 처리합니다.
    또한, 결과의 정확도를 높이기 위해 과정을 한번 더 반복 합니다.
    '''
    print('단어 프로세싱')
    print('첫번째 단계')
    task0 = [None] * len(audio_data_0)
    for i, val in enumerate(audio_data_0):
        task0[i] = threading.Thread(target=profane_recognizer.get_profane_time, args=(0, i, val[0], val[1]))
        task0[i].start()

    for i, _ in enumerate(audio_data_0):
        print(str(i + 1) + ' / ' + str(len(audio_data_0)))
        task0[i].join()

    print('두번째 단계')
    task1 = [None] * len(audio_data_1)
    for i, val in enumerate(audio_data_1):
        task1[i] = threading.Thread(target=profane_recognizer.get_profane_time, args=(1, i, val[0], val[1]))
        task1[i].start()

    for i, _ in enumerate(audio_data_1):
        print(str(i + 1) + ' / ' + str(len(audio_data_1)))
        task1[i].join()

    # 저장된 결과를 불러옵니다.
    print('결과 불러오기')
    result = utils.get_all_data()

    # 결과를 바탕으로 비프음 처리된 오디오를 생성합니다.
    print('오디오 생성')
    utils.generate_sound(result)

    #처리된 오디오와 기존의 영상을 합찹니다.
    print('결과 생성')
    utils.combine_audio_and_video(input_file)

    print('성공!')