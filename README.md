# user-management

Create docker image:

```bash
docker run -p 3307:3306 --name mysql -e MYSQL_ROOT_PASSWORD=toor -e MYSQL_DATABASE=user-management --rm -d mysql
```