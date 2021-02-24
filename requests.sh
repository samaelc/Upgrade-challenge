#!/usr/bin/env bash

curl --location --request POST 'http://localhost:8080/volcano-reservation/reservation' \
--header 'Content-Type: application/json' \
--data-raw '{
  "email": "albus.dumbledore@howarts.com",
  "fullName": "Albus Dumbledore",
  "startDate": "2021-02-26",
  "endDate": "2021-02-28"
}'\
&
curl --location --request POST 'http://localhost:8080/volcano-reservation/reservation' \
--header 'Content-Type: application/json' \
--data-raw '{
  "email": "severus.snape@howarts.com",
  "fullName": "Severus Snape",
  "startDate": "2021-02-26",
  "endDate": "2021-02-28"
}'\
&
curl --location --request POST 'http://localhost:8080/volcano-reservation/reservation' \
--header 'Content-Type: application/json' \
--data-raw '{
  "email": "minerva.mcgonagall@howarts.com",
  "fullName": "Minerva Mcgonagall",
  "startDate": "2021-02-26",
  "endDate": "2021-02-28"
}'