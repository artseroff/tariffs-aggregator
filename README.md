# Сервис для подбора тарифов мобильной связи
Проект представляет собой агрегатор тарифов операторов мобильной связи

Он позволяет пользователю выбирать тарифы мобильной связи разных операторов с помощью фильтрации по их характеристикам

Проект избавляет пользователя от необходимости посещать сайты разных операторов, собирая актуальные тарифы в одной информационной системе

На данный момент реализованы два приложения: tariffs-manager (информационная система) и megafon (микросервис)

В информационной системе представлены тарифы различных операторов, их характеристики, подробная информация о тарифах и компаниях-операторах

Неавторизованные пользователи выбирают тариф с помощью фильтров, просматривают каталоги. Редакторы каталогов изменяют информацию о тарифах и компаниях. Администраторы редактируют пользователей

Микросервис megafon с заданной периодичностью получает актуальные тарифы оператора Мегафон и отправляет их в информационную систему с помощью Kafka. У сайта этого оператора нет API, поэтому приходится парсить html страницы с помощью Jsoup. При обращении к сайту megafon.ru используется Retry 

Проект разработан на Java с использованием Spring MVC, шаблонизатора Thymeleaf, JPA (Hibernate) и БД PostgreSQL.
Сервисы взаимодействуют с помощью Apache Kafka.
Применен шаблон веб проектирования Post-Redirect-Get

# Скриншоты интерфейсов
Каталог компаний операторов мобильной связи:

![каталог_компаний](https://github.com/user-attachments/assets/419c21c1-f71d-4a19-b7a0-446c0f238b8d)

Каталог тарифов:

![каталог тарифов](https://github.com/user-attachments/assets/b7ead2e0-4cc2-4721-8b39-2111dcd8cf61)

Пример тарифов из каталога:

![пример тарифов](https://github.com/user-attachments/assets/12e72319-c601-4083-b7fe-d622c672fb0d)

Страница подробной информации о тарифе:

![тариф_подробнее](https://github.com/user-attachments/assets/ac80a228-1009-4d57-bcf1-9b88467f163e)

После авторизации как пользователь с ролью редактор каталогов будет доступен тот же функционал, но с возможностью редактирования контента

Страница редактирования каталога компаний:

![каталог компаний](https://github.com/user-attachments/assets/a3668362-e348-47f7-a4bf-b4bb110972f5)

Страница редактирования компании:

![компания](https://github.com/user-attachments/assets/14e57114-6c73-4bc3-9517-1cb75470acf3)

Страница редактирования каталога тарифов:

![каталог тарифов](https://github.com/user-attachments/assets/06bc0a6c-0325-4f6e-81f2-8fff286fd7a4)

Страница редактирования тарифа:

<img width="40%" src="https://github.com/user-attachments/assets/bb25968e-7550-4e4f-8fd7-edfad892a501">

После авторизации как пользователь с ролью администратор будет доступно добавление, удаление, редактирование пользователей

Список пользователей:

![список пользователей](https://github.com/user-attachments/assets/22ffea98-c21c-4168-a936-26fc948bb404)

Страница редактирования пользователя:

![пользователь](https://github.com/user-attachments/assets/a6de4547-176f-474e-a6aa-b660ac2a4d66)
