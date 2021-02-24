**Endpoints**
- Create reservation - POST http://localhost:8080/volcano-reservation/reservation
Request:
`{
   "email": "albus.dumbledore@howarts.com",
   "fullName": "Albus Dumbledore",
   "startDate": "2021-02-26",
   "endDate": "2021-02-28"
 }`
 Response:
 `{
      "id": "a84b67a2-632c-4093-8995-e1dd4e7e8347",
      "email": "albus.dumbledore@howarts.com",
      "fullName": "Albus Dumbledore",
      "startDate": "2021-02-26",
      "endDate": "2021-02-28"
  }`
- Get availabilities - GET http://localhost:8080/volcano-reservation/availability?startDate=2021-02-24
Response:
`[
     "2021-02-24",
     "2021-02-25",
     "2021-02-26",
     "2021-02-27",
     "2021-02-28",
     "2021-03-01",
     "2021-03-02",
     "2021-03-03",
     "2021-03-04",
     "2021-03-05",
     "2021-03-06",
     "2021-03-07",
     "2021-03-08",
     "2021-03-09",
     "2021-03-10",
     "2021-03-11",
     "2021-03-12",
     "2021-03-13",
     "2021-03-14",
     "2021-03-15",
     "2021-03-16",
     "2021-03-17",
     "2021-03-18",
     "2021-03-19",
     "2021-03-20",
     "2021-03-21",
     "2021-03-22",
     "2021-03-23"
 ]`
- Get reservation - GET http://localhost:8080/volcano-reservation/reservation/f8aa0abd-a340-4008-95df-774d60392781
Response:
`{
     "id": "c4ac615e-ac71-481d-a05f-a99ad2e77dfa",
     "email": "albus.dumbledore@howarts.com",
     "fullName": "Albus Dumbledore",
     "startDate": "2021-02-26",
     "endDate": "2021-02-28"
 }`
- Update reservation - PUT http://localhost:8080/volcano-reservation/reservation/f8aa0abd-a340-4008-95df-774d60392781
Request:
`{
   "email": "albus.dumbledore@howarts.com",
   "fullName": "Albus Dumbledore",
   "startDate": "2021-02-26",
   "endDate": "2021-02-28"
 }`
 Response:
 `{
      "id": "a84b67a2-632c-4093-8995-e1dd4e7e8347",
      "email": "albus.dumbledore@howarts.com",
      "fullName": "Albus Dumbledore",
      "startDate": "2021-02-26",
      "endDate": "2021-02-28"
  }`
- Cancel reservation - DELETE http://localhost:8080/volcano-reservation/reservation/"dba54066-6356-4255-942b-cb344741e54b"
Response:
`"dba54066-6356-4255-942b-cb344741e54b"`

**Concurrency test**
1. Start the application using the port 8080
2. Execute the file requests.sh
`
$ ./requests.sh
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100   449    0   314  100   135   9812   4218 --:--:-- --:--:-- --:--:-- 14031{"status":"BAD_REQUEST","message":"400 BAD_REQUEST \"Reservation not available for current dates\"; nested exception is com.samaelc.volcano.reservation.exception.ReservationNotAvailableException: Reservation not available for current dates","timestamp":"2021-02-23T21:00:42.2306267","errors":null,"subErrors":null}
100   443    0   314  100   129  10129   4161 --:--:-- --:--:-- --:--:-- 14290{"status":"BAD_REQUEST","message":"400 BAD_REQUEST \"Reservation not available for current dates\"; nested exception is com.samaelc.volcano.reservation.exception.ReservationNotAvailableException: Reservation not available for current dates","timestamp":"2021-02-23T21:00:42.2456273","errors":null,"subErrors":null}
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100   452    0   313  100   139  19562   8687 --:--:-- --:--:-- --:--:-- 28250{"status":"BAD_REQUEST","message":"400 BAD_REQUEST \"Reservation not available for current dates\"; nested exception is com.samaelc.volcano.reservation.exception.ReservationNotAvailableException: Reservation not available for current dates","timestamp":"2021-02-23T21:00:42.278625","errors":null,"subErrors":null}
`