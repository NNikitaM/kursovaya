-- Таблица Ролей
CREATE TABLE roles (
    role_Id SERIAL PRIMARY KEY,
    role_Name VARCHAR(50) UNIQUE NOT NULL
);

-- Таблица Клиентов
CREATE TABLE clients (
    client_Id SERIAL PRIMARY KEY,
    first_Name VARCHAR(50) NOT NULL,
    last_Name VARCHAR(50) NOT NULL,
    middle_Name VARCHAR(50) NULL,
    address VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    client_Type VARCHAR(50) NOT NULL,
    date_Of_Birth DATE NOT NULL,
    passport_Number VARCHAR(20) NOT NULL
);

-- Таблица Пользователей
CREATE TABLE users (
    user_Name VARCHAR(255) primary key,
    password VARCHAR(255) NOT NULL,
    client_Id INT REFERENCES clients(Client_Id) NOT NULL , 
    role_Id INT REFERENCES roles(Role_Id) NULL
);

-- Таблица Аккаунтов
CREATE TABLE accounts (
    account_Id SERIAL PRIMARY KEY,
    client_Id INT REFERENCES clients(Client_Id) NOT NULL ,
    account_Type VARCHAR(50) NOT NULL,
    balance DECIMAL(10,2) NOT NULL
);

-- Таблица Инструментов
CREATE TABLE instruments (
    instrument_Id SERIAL PRIMARY KEY,
    instrument_Type VARCHAR(50) NOT NULL,
    ticker_Symbol VARCHAR(10) NOT NULL,
    name VARCHAR(255) NOT NULL
);

-- Таблица Информации о Компании (для акций и облигаций)
CREATE TABLE company_Info (
    info_Id SERIAL PRIMARY KEY,
    instrument_Id INT REFERENCES instruments(instrument_Id) NOT NULL ,
    company_Name VARCHAR(255) NOT NULL,
    industry VARCHAR(50) NOT NULL,
    market_Cap DECIMAL(20,2) NOT NULL
);

-- Таблица Портфелей
CREATE TABLE portfolios (
    portfolio_Id SERIAL PRIMARY KEY,
    user_Name VARCHAR(255) REFERENCES users(user_Name) NOT NULL , 
    portfolio_Type VARCHAR(50) NULL,
    portfolio_Name VARCHAR(255) NOT NULL
);

-- Таблица Позиций Портфелей
CREATE TABLE portfolio_Positions (
    position_Id SERIAL PRIMARY KEY,
    portfolio_Id INT REFERENCES portfolios(portfolio_Id) NOT NULL ,
    instrument_Id INT REFERENCES instruments(instrument_Id) NOT NULL ,
    quantity INT NOT NULL,
    purchase_Price DECIMAL(10,2) NOT NULL,
    purchase_Date DATE
);

-- Таблица Операций
CREATE TABLE transactions (
    transaction_Id SERIAL PRIMARY KEY,
    account_Id INT REFERENCES accounts(account_Id) NOT NULL ,
    instrument_Id INT REFERENCES instruments(instrument_Id) NOT NULL ,
    transaction_Type VARCHAR(50) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    transaction_Date DATE not null
);

-- Таблица Комиссий
CREATE TABLE commissions (
    commission_Id SERIAL PRIMARY KEY,
    transaction_Id INT REFERENCES transactions(transaction_Id) NOT NULL,
    commission_Type VARCHAR(50) NOT NULL,
    amount DECIMAL(10,2) NOT NULL
);

-- Таблица Рыночных Данных
CREATE TABLE market_Data (
    data_Id SERIAL PRIMARY KEY,
    instrument_Id INT REFERENCES instruments(instrument_Id) NOT NULL,
    data_Date DATE NOT NULL,
    price DECIMAL(10,2) not null,
    volume INT not null
);

-- Таблица Инвестиционных Стратегий
CREATE TABLE investment_Strategies (
    strategy_Id SERIAL PRIMARY KEY,
    strategy_Name VARCHAR(255) NOT NULL,
    description VARCHAR,
    risk_Level VARCHAR(50) NOT NULL,
    return_Target DECIMAL(10,2) NOT NULL,
    asset_Allocation VARCHAR NOT NULL
);

-- Таблица Портфеля Стратегий
CREATE TABLE portfolio_Strategies (
    portfolio_Strategy_Id SERIAL PRIMARY KEY,
    portfolio_Id INT REFERENCES portfolios(portfolio_Id) NOT NULL,
    strategy_Id INT REFERENCES investment_Strategies(strategy_Id) NOT NULL
);

-- Таблица Отчетов
CREATE TABLE reports (
    report_Id SERIAL PRIMARY KEY,
    report_Type VARCHAR(50) NOT NULL,
    report_Date DATE NOT NULL,
    report_Content VARCHAR,
    user_Name VARCHAR(255) NOT NULL REFERENCES users(user_Name)
);

-- Вставка данных в таблицу ролей
INSERT INTO roles (role_Name) VALUES
('Администратор'),
('Пользователь'),
('Гость');

-- Вставка данных в таблицу клиентов
INSERT INTO clients (first_Name, last_Name, middle_Name, address, email, phone, client_Type, date_Of_Birth, passport_Number) VALUES
('Иван', 'Иванов', 'Иванович', 'ул. Пушкина, д. 10', 'ivan.ivanov@example.com', '+79001234567', 'Индивидуальный', '2000-01-01', '1234567890'),
('Петр', 'Петров', 'Петрович', 'пр. Ленина, д. 5', 'petr.petrov@example.com', '+79111234567', 'Корпоративный', '1995-05-15', '9876543210'),
('Мария', 'Сидорова', 'Петровна', 'ул. Лермонтова, д. 20', 'maria.sidorova@example.com', '+79221234567', 'Индивидуальный', '1998-08-28', '1111111111');

-- Вставка данных в таблицу пользователей
INSERT INTO users (user_Name, password, client_Id, role_Id) VALUES
('admin', '1', 1, 1),
('user', '2', 2, 2),
('guest', '3', 3, 3);

-- Вставка данных в таблицу аккаунтов
INSERT INTO accounts (client_Id, account_Type, balance) VALUES
(1, 'Текущий', 10000.00),
(2, 'Сберегательный', 5000.00),
(3, 'Текущий', 2000.00);

-- Вставка данных в таблицу инструментов
INSERT INTO instruments (instrument_Type, ticker_Symbol, name) VALUES
('Акция', 'AAPL', 'Apple Inc.'),
('Облигация', 'MSFT', 'Microsoft Corporation'),
('Валюта', 'EURUSD', 'Евро/Доллар США');

-- Вставка данных в таблицу информации о компании
INSERT INTO company_Info (instrument_Id, company_Name, industry, market_Cap) VALUES
(1, 'Apple Inc.', 'Технологии', 2.80E+12),
(2, 'Tesla Inc.', 'Автомобилестроение', 8.00E+11),
(3, 'Amazon.com Inc.', 'Розничная торговля', 1.20E+12);

-- Вставка данных в таблицу портфелей
INSERT INTO portfolios (user_Name, portfolio_Type, portfolio_Name) VALUES
('admin', 'Инвестиционный', 'Портфель администратора'),
('user', 'Торговый', 'Портфель пользователя 1'),
('guest', 'Сберегательный', 'Портфель гостя 1');

-- Вставка данных в таблицу позиций портфелей
INSERT INTO portfolio_Positions (portfolio_Id, instrument_Id, quantity, purchase_Price, purchase_Date) VALUES
(1, 1, 100, 150.00, '2023-03-15'),
(2, 2, 50, 250.00, '2023-03-10'),
(3, 3, 1000, 1.10, '2023-03-05');

-- Вставка данных в таблицу операций
INSERT INTO transactions (account_Id, instrument_Id, transaction_Type, amount, transaction_Date) VALUES
(1, 1, 'Покупка', 15000.00, '2023-03-15'),
(2, 2, 'Продажа', 12500.00, '2023-03-10'),
(3, 3, 'Обмен', 1100.00, '2023-03-05');

-- Вставка данных в таблицу комиссий
INSERT INTO commissions (transaction_Id, commission_Type, amount) VALUES
(1, 'Брокерская', 15.00),
(2, 'Биржевая', 10.00),
(3, 'Обменная', 5.00);

-- Вставка данных в таблицу рыночных данных
INSERT INTO market_Data (instrument_Id, data_Date, price, volume) VALUES
(1, '2023-03-15', 155.00, 10000),
(2, '2023-03-10', 260.00, 5000),
(3, '2023-03-05', 1.15, 20000);

-- Вставка данных в таблицу инвестиционных стратегий
INSERT INTO investment_Strategies (strategy_Name, description, risk_Level, return_Target, asset_Allocation) VALUES
('Долгосрочный рост', 'Инвестирование в акции с высокой капитализацией', 'Низкий', 0.08, '70% Акции, 20% Облигации, 10% Наличные'),
('Консервативная', 'Инвестирование в облигации с низким риском', 'Очень низкий', 0.05, '90% Облигации, 10% Наличные'),
('Агрессивная', 'Инвестирование в акции с высоким ростом', 'Высокий', 0.15, '100% Акции');

-- Вставка данных в таблицу портфеля стратегий
INSERT INTO portfolio_Strategies (portfolio_Id, strategy_Id) VALUES
(1, 1),
(2, 2),
(3, 3);

-- Вставка данных в таблицу отчетов
INSERT INTO reports (report_Type, report_Date, report_Content, user_Name) VALUES
('Ежедневный', '2023-03-15', 'Отчет о текущей стоимости портфеля', 'admin'),
('Еженедельный', '2023-03-12', 'Отчет о результатах торговли за неделю', 'user'),
('Ежемесячный', '2023-03-01', 'Отчет о движении средств на счете', 'guest');
