## steps to run app
```1) ./gradlew clean build -x test```

```2) ./gradlew bootrun```


Curl req:
```
curl --location --request POST 'http://localhost:8080/v1/pubsub-message' \
--header 'Content-Type: application/json' \
--data-raw ' {
        "variantId": "110012",
        "sellerId": "FALABELLA_CHILE"
}'
```