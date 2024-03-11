## Rest api с использованием фреймворка Spring.
Реализован rest api для перенаправления запросов на https://jsonplaceholder.typicode.com/ c базовой авторизацией, ролевой моделью доступа к данным, ведением аудита и кэшированием.


### Функциональные требования
1) Реализовать обработчики (GET, POST, PUT, DELETE), которые проксируют запросы к
https://jsonplaceholder.typicode.com/
    - /api/posts/**
    - /api/users/**
    - /api/albums/**
2) Базовая авторизация, с несколькими аккаунтами, у которых будут разные роли.
3) Проработать ролевую модель доступа. Чтобы было минимум 4 роли - ROLE_ADMIN, ROLE_POSTS, ROLE_USERS, ROLE_ALBUMS
    - ROLE_ADMIN -- имеет доступ ко всем обработчикам
    - ROLE_POSTS -- имеет доступ к обработчикам /posts/**
    - ROLE_USERS -- имеет доступ к обработчикам /users/**
    - ROLE_ALBUMS -- имеет доступ к обработчикам /albums/**
4) Реализовать ведение аудита действий. (дата-время, пользователь, имеет ли доступ, параметры запроса, ...).
5) Реализовать inmemory кэш, чтобы уменьшить число запросов к jsonplaceholder.

### Дополнительные возможности
0) Простота запуска приложения. Использован gradle. 
Для запуска можно использовать docker: 

        docker compose up

1) Использование базы данных: 
   - для ведением аудита,
   - для хранения данных пользователей. 
2) rest api для создания пользователей. 

Для создания нового пользователя нужно отправить POST запрос на endpoint "/register". В теле запроса необходимо передать json вида:

        {
            "username": String,
            "password": String,
            "role_privilege": [
                {
                    "role": String,
                    "privilege": String
                },
                {
                    "role": String,
                    "privilege": String
                },
                ...
            ]
        }

3) Расширенная ролевая модель. (например, ROLE_POSTS_VIEWER, ROLE_POSTS_EDITOR, ...). Иерархия:

        ADMIN_EDIT > POSTS_EDIT > POSTS_VIEW
        ADMIN_EDIT > USERS_EDIT > USERS_VIEW
        ADMIN_EDIT > ALBUMS_EDIT > ALBUMS_VIEW
        ADMIN_VIEW > POSTS_VIEW
        ADMIN_VIEW > USERS_VIEW
        ADMIN_VIEW > ALBUMS_VIEW

4) Юнит тесты на написанный код.
