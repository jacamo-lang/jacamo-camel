#!/bin/sh

gradle build

mv build/libs/jacamo-graphql-0.1.jar examples/main_example/libs/jacamo-graphql-0.1.jar

gradle -p examples/main_example/
