import google_cloud_api as gc_api

def get_profane_time(index1, index2, file_path_and_name, start_time):
    '''
    api에서 return 된 값에서 욕에 해당하는 단어만 선별하여 start, end 타임을 .txt로 저장합니다.
    :param index1: 몇 번째 단계 인지
    :param index2: 해당되는 단계에서 영상의 번호
    :param file_path_and_name: 파일의 경로와 이름
    :param start_time: 단어의 시작
    :param end_time: 단어의 끝점
    :return:
    '''
    word_and_startend_time = gc_api.transcribe_file_with_word_time_offsets(file_path_and_name)

    defined_profane_word = []
    f = open('profane_word.txt', 'r')
    defined_profane_word = f.read().split()

    profane_startend_time = []

    for word, start, end in word_and_startend_time:
        for profane_word in defined_profane_word:
            if word.find(profane_word) != -1:
                profane_startend_time += [[start_time + start, start_time + end]]

    f = open('detector_result/' + str(index1) + '_' + str(index2) + '.txt', 'w')
    for start, end in profane_startend_time:
        f.write(str(start)+','+str(end)+'\n')