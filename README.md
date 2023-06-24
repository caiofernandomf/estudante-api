# estudante-api
Projeto do curso Descomplicando Java e Spring da LinuxTips

# RUN(Linux):    
    ./mvnw spring-boot:run

## ⚙️ Project Dependencies
    - Lombok
    - MapStruct
    - H2

## 💻 Environment dependencies
    - Java 19     

## ROUTES

<br>

Rest/Restful architecture - HTTP - POST, GET, PUT, DELETE - CRUD.

<br>

### POST

<div align = "center">

| Method |                route                |      Description     |
|:------:|:-----------------------------------:|:--------------------:|
| `POST` | localhost:8080/v1/estudantes | Create a new student |

<br>
</div>

<div>
Example:

```json
[
  {
    "nome": "Ayrton",
    "endereco": "Lapa",
    "curso" : "Descomplicando SQL",
    "dadosBancarios" : {
      "agencia" : 123,
      "conta" : 4568,
      "digito": 3,
      "tipoContaBancaria": "POUPANCA"
    }
  }
]
```
</div>

### GET

<div align = "center">

| Method |                  route                   |                     Description                     |
|:------:|:----------------------------------------:|:---------------------------------------------------:|
| `GET`  |       localhost:8080/v1/estudantes       |                  List all students                  |
| `GET`  |    localhost:8080/v1/estudantes/{id}     |                List a student by Id                 |
| `GET`  | localhost:8080/v1/estudantes/nome/{name} |               List a student by name                |
| `GET`  | localhost:8080/v1/estudantes/nome?nome={name} | List all students that name contains a given string |
| `GET`  | localhost:8080/v1/estudantes/curso?curso={name} |             List all students by course           |


<br>
</div>

### PUT

<div align = "center">

| Method |               route               |   Description   |
|:------:|:---------------------------------:|:---------------:|
| `PUT`  | localhost:8080/v1/estudantes/{id} | Upate a student |

<br>
</div>

### DELETE

<div align = "center">

|  Method  |               route               |   Description    |
|:--------:|:---------------------------------:|:----------------:|
| `DELETE` | localhost:8080/v1/estudantes/{id} | Delete a student |

<br>
</div>