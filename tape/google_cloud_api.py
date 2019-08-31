import argparse
import io

def transcribe_file_with_word_time_offsets(speech_file):
    '''
    구글 클라우드 스피치 api를 이용하여 입력된 소리에서 사용자가 말한 단어의 시작과 끝점이 어디인지 return 합니다.
    :param speech_file: input 할 오디오 파일 경로와 이름입니다.
    :return: 사용자가 말한 단어들점의 시작점과 끝점을 포함한 리스트입니다.
    '''
    from google.cloud import speech
    from google.cloud.speech import enums
    from google.cloud.speech import types
    client = speech.SpeechClient()

    with io.open(speech_file, 'rb') as audio_file:
        content = audio_file.read()

    audio = types.RecognitionAudio(content=content)
    config = types.RecognitionConfig(
        encoding=enums.RecognitionConfig.AudioEncoding.LINEAR16,
        sample_rate_hertz=44100,
        language_code='ko-KR',
        enable_word_time_offsets=True)

    response = client.recognize(config, audio)

    word_and_startend_time = []

    for result in response.results:
        alternative = result.alternatives[0]

        for word_info in alternative.words:
            word = word_info.word
            start_time = word_info.start_time
            end_time = word_info.end_time
            word_and_startend_time += [[word, start_time.seconds + start_time.nanos * 1e-9, end_time.seconds + end_time.nanos * 1e-9]]

    return word_and_startend_time
