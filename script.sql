-- Таблица Ролей
CREATE TABLE roles (
    role_Id SERIAL PRIMARY KEY,
    role_Name VARCHAR(50) UNIQUE NOT NULL
);

-- Таблица Клиентов
CREATE TABLE clients (
    client_Id SERIAL PRIMARY KEY,
    first_Name VARCHAR(255) NOT NULL,
    last_Name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(20),
    client_Type VARCHAR(50),
    date_Of_Birth DATE,
    passport_Number VARCHAR(20)
);

-- Таблица Пользователей
CREATE TABLE users (
    user_Name VARCHAR(255) primary key,
    password VARCHAR(255) NOT NULL,
    client_Id INT NOT NULL REFERENCES clients(Client_Id), 
    role_Id INT NOT NULL REFERENCES roles(Role_Id)
);

-- Таблица Аккаунтов
CREATE TABLE accounts (
    account_Id SERIAL PRIMARY KEY,
    client_Id INT NOT NULL REFERENCES clients(Client_Id),
    account_Type VARCHAR(50) NOT NULL,
    balance DECIMAL(10,2) NOT NULL
);

-- Таблица Инструментов
CREATE TABLE instruments (
    instrument_Id SERIAL PRIMARY KEY,
    instrument_Type VARCHAR(50) NOT NULL,
    ticker_Symbol VARCHAR(10),
    name VARCHAR(255)
);

-- Таблица Информации о Компании (для акций и облигаций)
CREATE TABLE company_Info (
    info_Id SERIAL PRIMARY KEY,
    instrument_Id INT NOT NULL REFERENCES instruments(instrument_Id),
    company_Name VARCHAR(255),
    industry VARCHAR(50),
    market_Cap DECIMAL(20,2)
);

-- Таблица Портфелей
CREATE TABLE portfolios (
    portfolio_Id SERIAL PRIMARY KEY,
    user_Name INT NOT NULL REFERENCES users(user_Name), 
    portfolio_Type VARCHAR(50),
    portfolio_Name VARCHAR(255)
);

-- Таблица Позиций Портфелей
CREATE TABLE portfolio_Positions (
    position_Id SERIAL PRIMARY KEY,
    portfolio_Id INT NOT NULL REFERENCES portfolios(portfolio_Id),
    instrument_Id INT NOT NULL REFERENCES instruments(instrument_Id),
    quantity INT NOT NULL,
    purchase_Price DECIMAL(10,2) NOT NULL,
    purchase_Date DATE
);

-- Таблица Операций
CREATE TABLE transactions (
    transaction_Id SERIAL PRIMARY KEY,
    account_Id INT NOT NULL REFERENCES accounts(account_Id),
    instrument_Id INT NOT NULL REFERENCES instruments(instrument_Id),
    transaction_Type VARCHAR(50) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    transaction_Date DATE
);

-- Таблица Комиссий
CREATE TABLE commissions (
    commission_Id SERIAL PRIMARY KEY,
    transaction_Id INT NOT NULL REFERENCES transactions(transaction_Id),
    commission_Type VARCHAR(50) NOT NULL,
    amount DECIMAL(10,2) NOT NULL
);

-- Таблица Рыночных Данных
CREATE TABLE market_Data (
    data_Id SERIAL PRIMARY KEY,
    instrument_Id INT NOT NULL REFERENCES instruments(instrument_Id),
    data_Date DATE NOT NULL,
    price DECIMAL(10,2),
    volume INT
);

-- Таблица Инвестиционных Стратегий
CREATE TABLE investment_Strategies (
    strategy_Id SERIAL PRIMARY KEY,
    strategy_Name VARCHAR(255) NOT NULL,
    description TEXT,
    risk_Level VARCHAR(50),
    return_Target DECIMAL(10,2),
    asset_Allocation TEXT
);

-- Таблица Портфеля Стратегий
CREATE TABLE portfolio_Strategies (
    portfolio_Strategy_Id SERIAL PRIMARY KEY,
    portfolio_Id INT NOT NULL REFERENCES portfolios(portfolio_Id),
    strategy_Id INT NOT NULL REFERENCES investment_Strategies(strategy_Id)
);

-- Таблица Отчетов
CREATE TABLE reports (
    report_Id SERIAL PRIMARY KEY,
    report_Type VARCHAR(50) NOT NULL,
    report_Date DATE NOT NULL,
    report_Content TEXT,
    user_Name INT NOT NULL REFERENCES users(user_Name)
);

