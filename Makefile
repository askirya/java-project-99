.DEFAULT_GOAL := build-run

clean:
	./gradlew clean

build:
	./gradlew clean build

install:
	./gradlew installDist

run-dist:
	./build/install/app/bin/app

run:
	./gradlew bootRun --args='--spring.profiles.active=development'

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

lint:
	./gradlew checkstyleMain checkstyleTest

build-run: build run
