# User Management

## Running the Application
### Clone the repository:


```bash
git clone https://github.com/lyingparachute/user-management.git
cd user-management
```


### Create docker image:

```bash
docker run -p 3307:3306 --name mysql -e MYSQL_ROOT_PASSWORD=toor -e MYSQL_DATABASE=user-management --rm -d mysql
```

### Build and run the application:

```bash
./mvnw spring-boot:run
```

The application should now be running at http://localhost:8080.


## Endpoints

### Create User Details

Create a new user account.

- **URL:** `/user-details/{id}`
- **Method:** `POST`
- **Request Body:** `UserAccountRequest`
- **Response:**
    - `201 Created` with UserAccountResponse body if the user is created successfully.
    - `400 Bad Request` if the request body is invalid.

**Example Request:**

```shell
curl -X POST "http://localhost:8080/user-details" \
     -H "Content-Type: application/json" \
     -d '{
           "username": "johndoe",
           "email": "johndoe@example.com",
           "gender": "MALE",
           "age": 30
         }'

```

### Get User Details

Retrieve the details of a user by their ID.

- **URL:** `/user-details/{id}`
- **Method:** `GET`
- **Path Variable:** `id` (Long) - The ID of the user to retrieve.
- **Response:**
    - `200 OK` with `UserAccountResponse` body if the user exists.
    - `404 Not Found` if the user does not exist.

**Example Request:**

```shell
curl -X GET "http://localhost:8080/user-details/1"
```

### Update User Details

Update the details of an existing user.

- **URL:** `/user-details/{id}`
- **Method:** `PATCH`
- **Path Variable:** `id` (Long) - The ID of the user to update.
- **Request Body:** `UserAccountRequest`
- **Response:**
    - `200 OK` with `UserAccountResponse` body if the user updated successfully.
    - `400 Bad Request` if the request body is invalid or user not found.
    - `404 Not Found` if the user does not exist.

**Example Request:**

```shell
curl -X PATCH "http://localhost:8080/user-details/1" \
     -H "Content-Type: application/json" \
     -d '{
           "username": "johnupdated",
           "email": "johnupdated@example.com",
           "gender": "FEMALE",
           "age": 31
         }'

```

### DELETE User Details

Delete an existing user by their ID.

- **URL:** `/user-details/{id}`
- **Method:** `DELETE`
- **Path Variable:** `id` (Long) - The ID of the user to delete.
- **Response:**
    - `204 No Content`

**Example Request:**

```shell
curl -X DELETE "http://localhost:8080/user-details/1"
```