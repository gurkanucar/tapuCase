# Tapu Case Url Shortener Project

### Postman collection is available
  [Open](https://github.com/gurkanucar/tapuCase/blob/master/tapuCase.postman_collection.json)

### Check Swagger Api Documentation From This EndPoint
#### BASE_URL/swagger-ui.html#/





## Installation

```bash
  git clone https://github.com/gurkanucar/tapuCase.git

  cd tapuCase

  mvn spring-boot:run
```


## API Reference


#### Signup user

```http
  Post /user/signup
```
##### request:

```json
{
  "password": "pass",
  "username": "gurkan"
}
```
##### response:

```json
{
     "id": 1,
    "username": "gurkan"
}
```
###


#### Create Short Url

```http
  Post /s/user/{userId}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `userId` | `string` | **Required**. |

##### request:

* "code" is an optional value, not required !

```json
{
  "url": "http://gurkanucar.com",
  "code":"YOUR_CODE"
}
```
##### response:

```json
{
    "id": 1,
    "url": "http://gurkanucar.com",
    "code": "VTR4W",
    "shortened": "http://localhost:8080/s/VTR4W"
}
```
###



#### Open Url


```http
  GET /s/${code}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `code` | `string` | **Required**. |

##### request:

##### response:

status: 301 - MAKE_REDIRECT

###


#### Get All By User ID


```http
  GET /s/user/{userId}/list
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `userId` | `string` | **Required**. |

##### response:

```json
[
    {
        "id": 1,
        "url": "https://www.tapu.com/l/uygulamaya-ozel-kampanyali-tapular",
        "code": "TAPU",
        "shortened": "http://localhost:8080/s/TAPU"
    },
    {
        "id": 2,
        "url": "https://github.com/gurkanucar",
        "code": "github",
        "shortened": "http://localhost:8080/s/github"
    }
]
```

###


#### Get Details By UserId And ShortUrl Id


```http
  GET /s/user/{userId}/detail/{urlId}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `userId` | `string` | **Required**. |
| `urlId` | `string` | **Required**. |

##### response:

```json
    {
        "id": 1,
        "url": "https://www.tapu.com/l/uygulamaya-ozel-kampanyali-tapular",
        "code": "TAPU",
        "shortened": "http://localhost:8080/s/TAPU"
    }
```

###




#### Delete By UserId And ShortUrl Id


```http
  DELETE /s/user/{userId}/detail/{urlId}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `userId` | `string` | **Required**. |
| `urlId` | `string` | **Required**. |

##### response:

```json
    {
        "id": 1,
        "url": "https://www.tapu.com/l/uygulamaya-ozel-kampanyali-tapular",
        "code": "TAPU",
        "shortened": "http://localhost:8080/s/TAPU"
    }
```

###
