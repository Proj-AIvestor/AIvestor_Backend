# AIvestor Backend Spring Boot Application Dockerfile

# 빌드 스테이지
FROM gradle:7.6-jdk17 AS build
WORKDIR /app

# Gradle 래퍼와 설정 파일 복사
COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY gradle gradle

# 의존성 다운로드 (캐싱 활용)
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY src src

# 애플리케이션 빌드
RUN ./gradlew bootJar --no-daemon

# 실행 스테이지
FROM openjdk:17-jdk-slim
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 타임존 설정
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# JVM 메모리 설정을 위한 환경 변수
ENV JAVA_OPTS="-Xms512m -Xmx1024m"

# 포트 노출 (Spring Boot 기본 포트)
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]