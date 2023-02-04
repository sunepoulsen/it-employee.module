#!/bin/bash

echo 'Clean project'
echo '==================================================================================='
echo
./gradlew clean

echo 'Build projects'
echo '==================================================================================='
echo
./gradlew build

echo 'Build JavaDoc'
echo '==================================================================================='
echo
./gradlew javadoc

echo 'Run component tests'
echo '==================================================================================='
echo
./gradlew it-employee-component-tests:check -Pcomponent-tests

echo 'Run stress tests'
echo '==================================================================================='
echo
./gradlew it-employee-stress-tests:check -Pstress-tests -Dstress.test.profile=local

echo 'Publish artifacts'
echo '==================================================================================='
echo
./gradlew publish

echo 'Analyze project with SonarQube'
echo '==================================================================================='
echo
./gradlew sonarqube
