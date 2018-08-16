import json

from django.shortcuts import render, render_to_response
from django.http import HttpResponse
import pymysql
from django.views.decorators.csrf import csrf_exempt


def index(request):
    return HttpResponse("hello.")


def home(request):
    return render_to_response("test1.html")


def submit(request):
    name = request.GET['name']
    audit1 = request.GET['audit1']
    audit2 = request.GET['audit2']
    type1 = request.GET['type1']
    type2 = request.GET['type2']
    if (type1 == "yes"):
        t1 = "TINYINT(1)"
    else:
        t1 = "CHAR(20)"
    if (type2 == "yes"):
        t2 = "TINYINT(1)"
    else:
        t2 = "CHAR(20)"

    db = pymysql.connect("localhost", "root", "root", "audit")
    cursor = db.cursor()
    sql = "CREATE TABLE " + name + " ( " + audit1 + " " + t1 + " COLLATE utf8_unicode_ci NOT NULL," \
          + " " + audit2 + " " + t2 + " COLLATE utf8_unicode_ci NOT NULL" + " )"
    cursor.execute(sql)
    sql2 = "SELECT count(*) FROM information_schema.tables WHERE table_schema='audit'"
    cursor.execute(sql2)
    data = cursor.fetchall()
    print(data)
    print(data[0])
    print("\n")
    sql3 = "show tables"
    cursor.execute(sql3)
    print(cursor.fetchone())
    data = cursor.fetchall()
    for i in data:
        print(i[0])
    db.close()

    global num

    return HttpResponse("CREATE TABLE OK\n")


def dblist(request):
    db = pymysql.connect("localhost", "root", "root", "audit")
    cursor = db.cursor()
    sql1 = "SELECT count(*) FROM information_schema.tables WHERE table_schema='audit'"
    cursor.execute(sql1)
    count = cursor.fetchone()
    sql2 = "show tables"
    cursor.execute(sql2)
    data = cursor.fetchall()
    print(data)
    name = []
    for i in data:
        name.append(i[0])
    print(name)
    item_names = ""
    for j in range(count[0] - 11):
        print(j)
        item_names += name[j] + " "

    db.close()
    return HttpResponse(item_names)


def items(request, item_name):
    db = pymysql.connect("localhost", "root", "root", "audit")
    cursor = db.cursor()
    sql3 = "DESC " + item_name
    cursor.execute(sql3)
    desc = cursor.fetchall()
    print(desc)
    item = []
    type = []
    for i in desc:
        print(i[0] + "+" + i[1])
        item.append(i[0])
        if(i[1] == "tinyint(1)"):
            type.append("bool")
        else:
            type.append("text")
    print(",".join(item))

    db.close()
    return HttpResponse(toJson(item, type))


def toJson(item, type):
    return '{"items":"%s",' % ','.join(item) + '"type":"%s"}' % ','.join(type)


@csrf_exempt
def upload(request):
    print("start uploading...")
    str_post = request.body.decode('utf8')
    print(str_post)
    # str_post = '{"机房": {"温度": "526", "是否": "公交卡"}, "测试": {"项目1": "332", "项目2": "回来了"}}'
    str_json = json.loads(str_post)
    print(str_json)

    json_txt(str_json)
    print(tableName)
    global itemName, dataArray, tableNum
    print(itemName)
    print(dataArray)
    print(tableNum)

    toSQL()
    tableNum = 0
    itemName = [[] for i in range(MAX)]
    dataArray = [[] for j in range(MAX)]
    print("upload ok")

    return HttpResponse("upload ok")

MAX = 50
tableName = []
itemName = [[] for i in range(MAX)]
dataArray = [[] for j in range(MAX)]
tableNum = 0


def json_txt(dic_json):
    db = pymysql.connect("localhost", "root", "root", "audit")
    if isinstance(dic_json, dict):
        for i, key in enumerate(dic_json):
            if isinstance(dic_json[key], dict):
                print("****key--：%s value--: %s" % (key, dic_json[key]))

                tableName.append(key)

                # print(i)
                json_txt(dic_json[key])
                global tableNum
                tableNum += 1
            else:
                print("****key--：%s value--: %s" % (key, dic_json[key]))

                # print(i)
                itemName[tableNum].append(key)
                dataArray[tableNum].append(dic_json[key])
    db.close()


def toSQL():
    db = pymysql.connect("localhost", "root", "root", "audit")
    cursor = db.cursor()

    for i in range(tableNum):
        key = value = ""
        for j in range(len(itemName[i])):
            key += itemName[i][j]
            value += "'" + dataArray[i][j] + "'"
            if j < len(itemName[i]) - 1:
                key += ","
                value += ","
        sql_in = "INSERT INTO " + tableName[i] + " (" + key + ") VALUES (" + value + ")"
        print(sql_in)
        cursor.execute(sql_in)
        db.commit()
    sql_t = "SELECT * FROM 机房"
    cursor.execute(sql_t)
    test = cursor.fetchall()
    print(test)
