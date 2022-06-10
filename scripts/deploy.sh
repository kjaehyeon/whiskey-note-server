#!/bin/bash
REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=whiskey-note-server
API_MODULE_NAME=whiskey-note-api

echo "> Build 파일 복사"

cp $REPOSITORY/zip/*SNAPSHOT.jar $REPOSITORY/

echo "> 현재 실행 중인 어플리케이션 PID 확인"

CURRENT_PID=$(pgrep -fl ${API_MODULE_NAME} | grep jar | awk'{print $1}')

echo "> PID : $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
	echo "> 현재 진행 중인 어플리케이션이 없으므로 종료하지 않습니다."
else
	echo "> kill -15 $CURRENT_PID"
	kill -15 $CURRENT_PID
	sleep 5
fi

echo "> 새 어플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*SNAPSHOT.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

nohup java -jar -Dspring.config.location=classpath:/application.yml,classpath:/application-s3.properties,classpath:/application-jwt.properties,classpath:/application-db.yml  -Dspring.profiles.active=prd $REPOSITORY/$JAR_NAME 2>&1 &

