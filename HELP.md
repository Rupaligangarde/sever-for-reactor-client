# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.5/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.4.5/gradle-plugin/reference/html/#build-image)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

## steps to run app
```1) ./gradlew clean build -x test```
```2) ./gradlew bootrun```

```
curl --location --request POST 'http://localhost:8080/v1/pubsub-message' \
--header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6Ijc3MjA5MTA0Y2NkODkwYTVhZWRkNjczM2UwMjUyZjU0ZTg4MmYxM2MiLCJ0eXAiOiJKV1QifQ.eyJhdWQiOiIzMjU1NTk0MDU1OS5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsImF6cCI6ImFwcC1kZXBsb3ltZW50QHNlbGxlci1wbGF0Zm9ybXMtZGV2LmlhbS5nc2VydmljZWFjY291bnQuY29tIiwiZW1haWwiOiJhcHAtZGVwbG95bWVudEBzZWxsZXItcGxhdGZvcm1zLWRldi5pYW0uZ3NlcnZpY2VhY2NvdW50LmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJleHAiOjE2MjAzMjIwMjgsImlhdCI6MTYyMDMxODQyOCwiaXNzIjoiaHR0cHM6Ly9hY2NvdW50cy5nb29nbGUuY29tIiwic3ViIjoiMTE2MDYzOTczNzIwMDUyNDc1NDA5In0.ibKZP5k5bbZxxacg4DwT5LTrsi2MvMJqjhB9LQquoKMpMPcNpj9h_EGuKgc5HMgBn0Df76pPhU8RJm-LyxPd0m771TZCOGzlbZP-e0WA0xE4n5FbH5uS-wjCtsk_RdSALt49342r1d1HyZz_IF05bfxwBFfyKy8GNO4vDooy0_XJuvC1EUsyYTeJa_hfNHBl7uunbF8BD2IpMij_v_8lMsFQclDT-eilAsB9S_0lNRYVIzvjNJ2cW7SJD-3nB72br-4HBSuUhXp_--zXWjlkctzhPJcgdgcC5WR4dvP6-w9JKfD1mIhCJo6aL_AbZPAA_ae0BxtCX2hf00XZjGTBXQ' \
--header 'Content-Type: application/json' \
--data-raw ' {
"variantId": "110012",
"sellerId": "SOME_SELLER",
"offeringId": "110011",
"stockGeoTypeKey": "Facility",
"stockGeoTypeId": "2103",
"shippingOptionType": "SiteToStore",
"stateOfStock": "InStock",
"hasStock": true,
"quantity": 100,
"sourceUpdatedAt": "2020-07-18T11:33:34.000Z"
}'
```