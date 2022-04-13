
# Currency Exchange - REST API

API is created to support current, historical and statistical exchange rates data.


## API Reference

#### Get current exchange rate for base and target

```http
  GET /api/current/{base}/{target}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `base`    | `string` | **Required** base          |
| `target`  | `string` | **Required** target        |

#### Example
```http
http://localhost:8080/api/current/USD/EUR
```
#### JSON Result
```json
{
  "firstCurrency": "USD",
  "secondCurrency": "EUR",
  "date": "2022-04-13",
  "result": 0.923719
}
```

#### Get historical exchange rate for base, target and date

```http
  GET /api/historica/{base}/{target}/{date}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `base`      | `string` | **Required**. base |
| `target`      | `string` | **Required**. target |
| `date`      | `string` | **Required**. date format "yyyy-mm-dd |

#### Example
```http
http://localhost:8080/api/historical/USD/EUR/2022-03-26
```
#### JSON Result
```json
{
  "firstCurrency": "USD",
  "secondCurrency": "EUR",
  "date": "2022-03-26",
  "result": 0.910363
}
```

#### Get statisctical exchange rate for base, target, date(from) and date(to)

```http
  GET /api/statisctical/{base}/{target}/{from}/{to}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `base`      | `string` | **Required**. base |
| `target`      | `string` | **Required**. target |
| `from`      | `string` | **Required**. date format "yyyy-mm-dd |
| `to`      | `string` | **Required**. date format "yyyy-mm-dd |

#### Example
```http
http://localhost:8080/api/statistical/USD/EUR/2022-03-10/2022-03-13
```
#### JSON Result
```json
[
  {
    "firstCurrency": "USD",
    "secondCurrency": "EUR",
    "date": "2022-03-10",
    "result": 0.90976
  },
  {
    "firstCurrency": "USD",
    "secondCurrency": "EUR",
    "date": "2022-03-11",
    "result": 0.916263
  },
  {
    "firstCurrency": "USD",
    "secondCurrency": "EUR",
    "date": "2022-03-12",
    "result": 0.916744
  },
  {
    "firstCurrency": "USD",
    "secondCurrency": "EUR",
    "date": "2022-03-13",
    "result": 0.914844
  }
]
```
## Tech Stack

Spring Boot, H2, Swagger


## Authors

- [@pawelbarwinski](https://github.com/PawelB-93)
- [@juliuszgorzen](https://github.com/Juliusz-G)

