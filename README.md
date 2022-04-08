# news-getter-springboot

Run project:

    docker-compose build
    docker-compose up
    (commands.sh file must be in LF instead of CRLF)

Tear Down:
    
    docker-compose down

Swagger default URL :

    http://<IP_ADDRESS>:8082/swagger-ui.html - GUI
    http://<IP_ADDRESS>:8082/v2/api-docs - Json Info

Endpoint Get All Articles Example:

    http://<IP_ADDRESS>:8082/api/articles
    (Other endpoints can be seen in swagger)

