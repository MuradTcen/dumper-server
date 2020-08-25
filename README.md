```
-- Команды для создания и удаления тестовой БД
drop database TestDB;
create database TestDB;

-- Команда для очистки истории бэкапов
use msdb;
go
declare @DATE DATETIME
SET @DATE = GETDATE() + 1
EXEC sp_delete_backuphistory @DATE;
go

-- Набор команд для заполнения данными таблицы и создания бэкапов
use TestDB;
go
drop table inventory;
go
CREATE TABLE inventory (id INT IDENTITY(1,1) PRIMARY KEY, product VARCHAR(50) UNIQUE, quantity INT, price DECIMAL(18,2) );
go
backup database [TestDB] to disk = N'/home/tcen/dumps/server/full.bak';
go
insert into inventory (product, quantity, price) values ('apple', 10, 10);
insert into inventory (product, quantity, price) values ('pinapple', 20, 20);
insert into inventory (product, quantity, price) values ('orange', 30, 30);
go
backup database [TestDB] to disk = N'/home/tcen/dumps/server/diff_0.bak' with differential;
insert into inventory (product, quantity, price) values ('pinorange', 40, 40);
backup database [TestDB] to disk = N'/home/tcen/dumps/server/diff_1.bak' with differential;
insert into inventory (product, quantity, price) values ('asdpinorange', 60, 60);
backup log [TestDB] to disk = N'/home/tcen/dumps/server/log_0.trn';
insert into inventory (product, quantity, price) values ('apinorange', 50, 50);
backup log [TestDB] to disk = N'/home/tcen/dumps/server/log_1.trn';

-- Набор команд для восстановления бэкапов

use master;
restore database [TestDB] from disk=N'/home/tcen/dumps/server/full.bak' with norecovery, replace;
go
restore database [TestDB] from disk=N'/home/tcen/dumps/server/diff_0.bak' with norecovery;
go
restore database [TestDB] from disk=N'/home/tcen/dumps/server/diff_1.bak' with norecovery;
go
restore log [TestDB] from disk=N'/home/tcen/dumps/server/log_0.trn' with norecovery;
go
restore log [TestDB] from disk=N'/home/tcen/dumps/server/log_1.trn' with norecovery;
go
restore database [TestDB] with recovery;
go
```

Путь к бэкапам в application.properties переменная ```directory```

Пример запроса, который успешно завершился:

[![imageup.ru](https://imageup.ru/img248/3645699/otvetvsluchaeuspexa.png)](https://imageup.ru/img248/3645699/otvetvsluchaeuspexa.png.html)