from django.shortcuts import render
from django.http.response import JsonResponse, HttpResponseBadRequest
from rest_framework.parsers import FormParser, MultiPartParser
from rest_framework import serializers, viewsets
from .models import *
from django.contrib.auth.models import User
from django.contrib import auth
from django.views.decorators.csrf import csrf_exempt
# Create your views here.


class ProfileSerializer(serializers.ModelSerializer):

    class Meta:
        model = Profile
        fields = '__all__'

class ProfileViewSet(viewsets.ModelViewSet):
    queryset = Profile.objects.all()
    serializer_class = ProfileSerializer

class VideoSerializer(serializers.ModelSerializer):
    class Meta:
        model = Video
        fields = ['user', 'title', 'filepath', 'pub_date', 'thumbnail', 'description']

class VideoViewSet(viewsets.ModelViewSet):
    queryset = Video.objects.all()
    serializer_class = VideoSerializer
    parser_classes = (MultiPartParser, FormParser,)
    read_only_fields = ('user',)

    def perform_create(self, serializer):
        """
        1. upload된 영상 파일을 os tape input file로 전달.
        2. os 모듈로 main.py 실행.
        3. upload 영상 filtering.
        4. filtering된 영상 db에 가져와 저장.
        5. app 화면에는 filtering된 영상 업로드.
        """
        # os tape api main.py
        video_field = self.request.data.get('filepath')
        # os로 tape input file 넣기
        # os main.py 실행
        # filter_file = os로 result에 생성된 영상 파일 load
        # queryset.filterpath = filter_file
        serializer.save(
            user=User.objects.get(pk=self.request.data.get('user')),
            title=self.request.data.get('title'),
            filepath=self.request.data.get('filepath'),
            thumbnail=self.request.data.get('thumbnail'),
            description=self.request.data.get('description'))

@csrf_exempt
def login(request):
    try:
        user = auth.authenticate(request, username=request.POST['phone'], password=request.POST['password'])
        if user is not None:
            auth.login(request, user)
            return JsonResponse({
                'id': user.id,
            })
        raise ValueError('user not found or incorrect password!')
    except Exception as ex:
        return JsonResponse({
            'error': str(ex),
        })

@csrf_exempt
def signup(request):
    try:
        user = User.objects.create_user(username=request.POST['phone'], password=request.POST['password'])
        user.profile.name = request.POST['name']
        user.profile.passwordQuestion = request.POST['password_question']
        user.profile.passwordQuestionAnswer = request.POST['password_question_answer']
        auth.login(request, user)
        return JsonResponse({
            'id': user.id,
        })
    except Exception as ex:
        return JsonResponse({
            'error': str(ex),
        })
