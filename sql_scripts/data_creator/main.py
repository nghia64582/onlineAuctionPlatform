import mysql.connector
import datetime as dt
import random as rd
from time import *

# Replace with your actual database credentials
host = "localhost" 
user = "root"
password = "admin123456"
database = "auctions"

NEW_LINE_INSERT = "INSERT INTO %s VALUES\n"

import re
from unidecode import unidecode

def generate_random_vietnamese_name(n=10000):

    # Define lists of common Vietnamese surnames and given names
    surnames = ["Nguyễn", "Trần", "Lê", "Phạm", "Vũ", "Võ", "Hoàng", "Đặng", "Bùi", "Hồ"]
    male_given_names = ["Hoàng", "Minh", "Anh", "Dũng", "Tuấn", "Khánh", "Việt", "Trung", "Hiếu", "Nam"]
    female_given_names = ["Hương", "Lan", "Hồng", "Mai", "Linh", "Thảo", "Ngọc", "Thanh", "Diệu"]
    male_second_names = ["Văn", "Trung", "Mạnh", "Hữu"]
    female_second_names = ["Thị", "Thu", "Hà"]

    names = []
    for _ in range(n):
        # Randomly select surname
        surname = rd.choice(surnames)

        # Determine gender (simplified for demonstration)
        is_male = rd.choice([True, False]) 

        # Select given name based on gender
        given_name = rd.choice(male_given_names) if is_male else rd.choice(female_given_names)

        second_name = rd.choice(male_second_names) if is_male else rd.choice(female_second_names)

        # Combine surname and given name
        name = surname + " " + second_name + " " + given_name
        names.append(name)

    return names

def convert_to_ascii(vietnamese_name):

    return unidecode(vietnamese_name)

def convert_name_to_short_form(full_name):

    # Vũ Trung Nghĩa => nghiavt
    # Trần Quang Đạt => dattq

    # 1. Convert to lowercase
    lower_name = full_name.lower()

    # 2. Remove diacritics (optional)
    # You can use a library like unidecode for this
    lower_name = unidecode(lower_name) 

    # 3. Extract the last name component
    last_name = re.findall(r'\b\w+\b$', lower_name, re.UNICODE)[0] 


    # 5. Combine with the first letter of other names
    first_letters = ''.join([name[0] for name in lower_name.split()[:-1]])

    # 6. Concatenate and return
    return last_name + first_letters

def convert_name_to_random_email(full_name):
    # Vũ Trung Nghĩa => nghiavt42729@gmail.com
    # Lê Thị Huyền => huyenlt59283@gmail.com
    return convert_name_to_short_form(full_name) + str(rd.randint(10000, 99999)) + "@gmail.com"

def get_random_location():
    LOCATIONS = [
        "An Giang", "Bà Rịa - Vũng Tàu", "Bắc Giang", "Bắc Kạn", "Bạc Liêu", "Bắc Ninh",
        "Bến Tre", "Bình Định", "Bình Dương", "Bình Phước", "Bình Thuận", "Cà Mau",
        "Cần Thơ", "Cao Bằng", "Đà Nẵng", "Đắk Lắk", "Đắk Nông", "Điện Biên",
        "Đồng Nai", "Đồng Tháp", "Gia Lai", "Hà Giang", "Hà Nam", "Hà Nội",
        "Hà Tĩnh", "Hải Dương", "Hải Phòng", "Hậu Giang", "Hòa Bình", "Hưng Yên",
        "Khánh Hòa", "Kiên Giang", "Kon Tum", "Lai Châu", "Lâm Đồng", "Lạng Sơn",
        "Lào Cai", "Long An", "Nam Định", "Nghệ An", "Ninh Bình", "Ninh Thuận",
        "Phú Thọ", "Phú Yên", "Quảng Bình", "Quảng Nam", "Quảng Ngãi", "Quảng Ninh",
        "Quảng Trị", "Sóc Trăng", "Sơn La", "Tây Ninh", "Thái Bình", "Thái Nguyên",
        "Thanh Hóa", "Thừa Thiên Huế", "Tiền Giang", "TP. Hồ Chí Minh", "Trà Vinh", "Tuyên Quang",
        "Vĩnh Long", "Vĩnh Phúc", "Yên Bái"
    ]
    weights = [10 if location in ["Hà Nội", "TP. Hồ Chí Minh"] else 1 for location in LOCATIONS]
    return rd.choices(LOCATIONS, weights=weights, k = 1)[0]

def get_random_state():
    STATES = ["PENDING", "AUCTIONING", "SOLD"]
    return rd.choice(STATES)

def get_random_cash():
    return rd.randint(1000, 10000)

def get_random_date():
    return dt.datetime(rd.randint(2020, 2024), rd.randint(1, 12), rd.randint(1, 28)) + dt.timedelta(seconds=rd.randint(1, 80000))

def get_number(st):
    return re.findall(r'\d+', st)

def create_random_categories():
    global N_CATE, categories
    categories = []
    VOWEL = "aueio"
    CONSONANT = "mnprtlkhgdsvb"
    LAST = "mntpc"
    def get_random_key():
        return rd.choice(CONSONANT) + rd.choice(VOWEL) + rd.choice(LAST)
    
    for i in range(N_CATE):
        id = i
        first_key = get_random_key()
        second_key = get_random_key()
        categories.append(f"cate_{id}_{first_key}_{second_key}")
    return categories

def init_connection():
    global conn, cursor
    # Connect to the MySQL database
    conn = mysql.connector.connect(
        host=host,
        user=user,
        password=password,
        database=database
    )

    # Create a cursor object
    cursor = conn.cursor()

def get_random_role():
    ROLES = ["AUCTIONEER", "BIDDER", "ADMIN"]
    probabilities = [0.1, 0.89, 0.01]  # Probabilities must sum up to 1

    return rd.choices(ROLES, weights = probabilities, k=1)[0]

def find_user_by_id(id):
    global users_dict
    return users_dict[id]

def create_random_users():
    global users, bidders, bidden_prices, auctioneers, categories, N_USERS, users_dict
    users = []
    users_dict = {}
    bidders = []
    auctioneers = []
    bidden_prices = []
    categories = []
    names = generate_random_vietnamese_name(N_USERS)
    mark_username = set()
    for i in range(N_USERS):
        id = i + 1
        name = names[i]
        username = convert_name_to_short_form(name) + str(rd.randint(1000, 9999))
        if username in mark_username:
            continue
        mark_username.add(username)
        password = "{noop}123456"
        enabled = 1
        age = rd.randint(18, 50)
        location = get_random_location()
        cash = rd.randint(10000, 99999)
        email = convert_name_to_random_email(name)
        created_date = get_random_date()
        last_login = created_date
        role = get_random_role()
        user = {
            "id": id,
            "name": name,
            "username": username,
            "password": password,
            "enabled": enabled,
            "email": email,
            "name": name,
            "age": age,
            "location": location,
            "cash": cash,            
            "created_date": created_date,
            "last_login": last_login,
            "role": role,
            "n_sold_product": 0,
            "n_bought_product": 0
        }
        users.append(user)
        users_dict[user["id"]] = user
        if role == "BIDDER":
            user["n_bought_product"] = 0
            bidders.append(user)
        if role == "AUCTIONEER":
            user["credit"] = 0
            user["n_sold_product"] = 0
            auctioneers.append(user)
    print("Finish create random users's data")

def create_random_products():
    global products

    products = []
    for i in range(N_PRODUCTS):
        id = i + 1
        auctioneer_id = rd.choice(auctioneers)["id"]
        name = f"product_name_{i}"
        image_url = f"/images/products/product_{id}.png"
        beginning_price = rd.randint(1000, 9999)
        current_price = beginning_price + rd.randint(1000, 9999)
        created_date = get_random_date()
        sold_date = created_date + dt.timedelta(seconds=rd.randint(100000, 1000000))
        state = get_random_state()
        location = get_random_location()
        product = {
            "id": id,
            "auctioneer_id": auctioneer_id,
            "name": name,
            "image_url": image_url,
            "beginning_price": beginning_price,
            "current_price": current_price,
            "created_date": created_date,
            "sold_date": sold_date,
            "state": state,
            "location": location
        }
        if state == "SOLD":
            product["bidder_id"] = rd.choice(bidders)["id"]
            auctioneer = find_user_by_id(product["auctioneer_id"])
            bidder = find_user_by_id(product["bidder_id"])
            auctioneer["n_sold_product"] += 1
            auctioneer["credit"] += rd.randint(5, 20)
            bidder["n_bought_product"] += 1
        products.append(product)
    print("Finish create random products's data")

def create_random_bidden_prices():
    global users, products, bidders, bidden_prices

    bidden_prices = []
    count_id = 0
    rd.shuffle(products)
    for product in products:
        product_id = product["id"]
        subset_bidder_ids = [bidder["id"] for bidder in rd.sample(bidders, rd.randint(3, 10))]
        n_bids = rd.randint(5, 20)
        previous_bidder_id = -1
        previous_price = -1
        previous_datetime = get_random_date()
        for _ in range(n_bids):
            while True:
                bidder_id = rd.choice(subset_bidder_ids)
                if bidder_id != previous_bidder_id:
                    break
            count_id += 1
            price = previous_price + rd.randint(100, 1000)
            created_date = previous_datetime + dt.timedelta(seconds=rd.randint(1000, 8000))
            bidden_price = {
                "id": count_id,
                "bidder_id": bidder_id,
                "product_id": product_id,
                "price": price,
                "created_date": created_date
            }
            bidden_prices.append(bidden_price)
            previous_bidder_id = bidder_id
            previous_datetime = created_date
            previous_price = price
    print("Finish create random bidden prices's data")

def create_random_product_categories():
    global products, categories, product_categories
    product_categories = []
    count = 0
    for product in products:
        self_categories = rd.choices([i + 1 for i in range(len(categories))], k = rd.randint(3, 6))
        for category in self_categories:
            count += 1
            product_category = {
                "id": count,
                "product_id": product["id"],
                "category_id": category
            }
            product_categories.append(product_category)

def generate_insert_product_query():    
    global products, auctioneers, N_PRODUCTS
    
    lines = []
    # ----------------------
    for product in products:
        id = product["id"]
        auctioneer_id = product["auctioneer_id"]
        name = product["name"]
        image_url = product["image_url"]
        beginning_price = product["beginning_price"]
        current_price = product["current_price"]
        created_date = product["created_date"]
        state = product["state"]
        location = product["location"]
        query = f"({id}, {auctioneer_id}, '{name}', '{image_url}', {beginning_price}, {current_price}, '{created_date}', '{state}', '{location}')"
        lines.append(query)
    write_data_to_file("sql_scripts/insert_product_queries.sql", lines, "product")
    print("Finish product query")

def generate_insert_bidder_query():
    global bidders
    
    lines = []
    for user in bidders:
        id = user["id"]
        n_bought_product = user["n_bought_product"]
        query = f"({id}, {n_bought_product})"
        lines.append(query)

    write_data_to_file("sql_scripts/insert_bidder_queries.sql", lines, "bidder")
    print("Finish bidder query")

def generate_insert_auctioneer_query():
    global auctioneers
    
    lines = []
    for user in auctioneers:
        id = user["id"]
        credit = user["credit"]
        n_sold_product = user["n_sold_product"]
        query = f"({id}, {credit}, {n_sold_product})"
        lines.append(query)
        
    write_data_to_file("sql_scripts/insert_auctioneer_queries.sql", lines, "auctioneer")
    print("Finish auctioneer query")

def generate_insert_users_query():
    global users
    name_marker = set()
    
    count_users = 0
    lines = []
    for i in range(len(users)):
        user = users[i]
        id = user["id"]
        name = user["name"]
        username = user["username"]
        if username in name_marker:
            continue
        else:
            name_marker.add(username)
        count_users += 1
        password = user["password"]
        enabled = user["enabled"]
        email = user["email"]
        created_date = user["created_date"]
        last_login = user["last_login"]
        role = user["role"]
        age = user["age"]
        location = user["location"]
        cash = user["cash"]
        query = f"({id}, '{username}', '{password}', {enabled}, '{email}', {age}, '{name}', '{location}', {cash}, '{created_date}', '{last_login}', '{role}')"
        lines.append(query)
        
    write_data_to_file("sql_scripts/insert_user_queries.sql", lines, "user")
    print(f"Finish users query, created {count_users}")

def generate_insert_authority_query():
    global users
    lines = []
    for user in users:
        id = user["id"]
        username = user["username"]
        authority = f"ROLE_{user["role"]}"
        query = f"({id}, '{username}', '{authority}')"
        lines.append(query)
        
    write_data_to_file("sql_scripts/insert_authority_queries.sql", lines, "authority")
    print(f"Finish authority query")

def generate_insert_category_query():    
    global categories

    lines = []
    for i in range(len(categories)):
        id = i + 1
        name = categories[i]
        query = f"({id}, '{name}')"
        lines.append(query)
        
    write_data_to_file("sql_scripts/insert_category_queries.sql", lines, "category")
    print("Finish category query")

def generate_insert_product_category_query():
    global product_categories
    lines = []
    for ele in product_categories:
        id = ele["id"]
        product_id = ele["product_id"]
        category_id = ele["category_id"]
        query = f"({id}, {product_id}, '{category_id}')"
        lines.append(query)
        
    write_data_to_file("sql_scripts/insert_product_categories_queries.sql", lines, "product_category")
    print("Finish product category query")

def generate_insert_sold_product_query():
    global products, bidders
    
    lines = []
    for product in products:
        if product["state"] == "SOLD":
            bidder = rd.choice(bidders)
            product_id = product["id"]
            sold_date = product["sold_date"]
            bidder_id = bidder["id"]
            query = f"({product_id}, '{sold_date}', {bidder_id})"
            lines.append(query)
        
    write_data_to_file("sql_scripts/insert_sold_product_queries.sql", lines, "sold_product")
    print("Finish sold product query")

def generate_insert_bidden_price_query():
    global bidden_prices

    lines = []
    for idx in range(len(bidden_prices)):
        bidden_price = bidden_prices[idx]
        id = bidden_price["id"]
        bidder_id = bidden_price["bidder_id"]
        product_id = bidden_price["product_id"]
        price = bidden_price["price"]
        created_date = bidden_price["created_date"]
        query = f"({id}, {bidder_id}, {product_id}, {price}, '{created_date}')"
        lines.append(query)
    write_data_to_file("sql_scripts/insert_bidden_price_queries.sql", lines, "bidden_price")
    print("Finish bidden prices query")

def write_data_to_file(file_path, lines, table_name):
    file = open(file_path, "w", encoding = 'utf-8')
    new_line_insert = f"INSERT INTO {table_name} VALUES\n"
    file.write(new_line_insert)
    for i in range(len(lines)):
        line = lines[i]
        line += ";" if i % 1000 == 199 or i == len(lines) - 1 else ","
        file.write(line + "\n")
        if i % 1000 == 199 and i != len(lines) - 1:
            file.write(new_line_insert + "\n")
    file.close()

def test_bidder():
    # Connect to the MySQL database
    conn = mysql.connector.connect(
        host=host,
        user=user,
        password=password,
        database=database
    )

    # Create a cursor object
    cursor = conn.cursor(buffered = True)

    start = time()
    for _ in range(10):
        print(f"Time {_}")
        for i in range(1, 1000):
            query = f"SELECT * FROM product WHERE id = {i};"
            # cursor.execute(query) # create new execution plan for each query

            # reuse execution plan
            thresh_hold = rd.randint(1000, 10000)
            cursor.execute("SELECT * FROM product WHERE beginning_price = %s;", (thresh_hold, ))
            # rows = cursor.fetchall()
    print(time() - start)

def execute_query_file(file_path):
    global conn, cursor
    file = open(file_path, "r", encoding='utf-8')
    start = time()
    count_error = 0
    try:
        lines = file.read().split("\n")
        query = ""
        for i in range(len(lines)):
            if i % 1000 == 0:
                print(f"Execute line {i} file {file_path}")
            line = lines[i]
            while len(line) > 0 and line[-1] == " ":
                line = line[:-1]
            query += line
            if len(line) > 0 and line[-1] == ';':
                try:
                    cursor.execute(query)
                    query = ""
                except Exception as e:
                    if count_error < 10:
                        print(f"Error at line {i}: {e}, query : {query}")
                    count_error += 1
        conn.commit()
        print(f"Finished execute file {file_path}, queries count : {len(lines)}, count error : {count_error}, total time : {time() - start}")
    except Exception as e:
        print("Exception in query ")
        print(e)
        sleep(10)

def reset_data():
    execute_query_file("sql_scripts/reset_data.sql")

def run_create_data():
    execute_query_file("sql_scripts/insert_user_queries.sql")
    execute_query_file("sql_scripts/insert_auctioneer_queries.sql")
    execute_query_file("sql_scripts/insert_authority_queries.sql")
    execute_query_file("sql_scripts/insert_product_queries.sql")
    execute_query_file("sql_scripts/insert_bidder_queries.sql")
    execute_query_file("sql_scripts/insert_bidden_price_queries.sql")
    execute_query_file("sql_scripts/insert_category_queries.sql")
    execute_query_file("sql_scripts/insert_product_categories_queries.sql")
    execute_query_file("sql_scripts/insert_sold_product_queries.sql")

def config():
    global N_USERS, N_PRODUCTS, N_CATE

    N_USERS = 10000
    N_PRODUCTS = 20000
    N_CATE = 50

def create_random_data():
    create_random_users()
    create_random_products()
    create_random_categories()
    create_random_bidden_prices()
    create_random_product_categories()

def generate_insert_query():
    generate_insert_users_query()
    generate_insert_authority_query()
    generate_insert_bidder_query()
    generate_insert_auctioneer_query()
    generate_insert_product_query()
    generate_insert_authority_query()
    generate_insert_category_query()
    generate_insert_bidden_price_query()
    generate_insert_product_category_query()
    generate_insert_sold_product_query()

if __name__ == "__main__":
    start = time()
    config()
    init_connection()
    create_random_data()
    generate_insert_query()
    reset_data()
    run_create_data()

    # test_bidder()

    print("Time:", time() - start)