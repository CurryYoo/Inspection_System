import io
import json

import xlwt as xlwt
from django.shortcuts import render, render_to_response
from django.http import HttpResponse
import pymysql
from django.views.decorators.csrf import csrf_exempt


def home(request):
    return render_to_response("home.html")


def display(request):
    sql = ""
    db = pymysql.connect("localhost", "root", "root", "audit")
    cursor = db.cursor()
    cursor.execute(sql)
    result = cursor.fetchall()

    response = HttpResponse(content_type='application/vnd.ms-excel')  # 指定返回为excel文件
    response['Content-Disposition'] = 'attachment;filename=export_agencycustomer.xls'  # 指定返回文件名
    wb = xlwt.Workbook(encoding='utf-8')  # 设定编码类型为utf8
    sheet = wb.add_sheet(u'类别')  # excel里添加类别

    sheet.write(0, 0, 'id')
    sheet.write(0, 1, '名字')

    row = 1
    for list in result:
        sheet.write(row, 0, list['id'])
        sheet.write(row, 1, list['label'])
        row = row + 1

    output = io.BytesIO()

    wb.save(output)
    output.seek(0)
    response.write(output.getvalue())
    return response


def add(request):
    return render_to_response("input.html")


def submit(request):
    db = pymysql.connect("localhost", "root", "root", "audit")
    cursor = db.cursor()

    name = request.GET['name']

    get = request.GET.copy()
    get.pop('name')
    print(len(get))
    for i in range(len(get) // 2):  # use '//' to transfer the result to integer (if use '/', the result's type is float)
        input_ = request.GET['input' + str(i)]
        type_ = request.GET['type' + str(i)]
        if(type_ == "yes"):
            # global type_c
            type_c = "TINYINT(1)"
        else:
            type_c = "CHAR(20)"
        sql_start = "CREATE TABLE " + name + " ( " + input_ + " " + type_c + " COLLATE utf8_unicode_ci NOT NULL" + " )"
        sql = "ALTER TABLE " + name + " ADD COLUMN " + input_ + " " + type_c + " COLLATE utf8_unicode_ci NOT NULL"
        print(str(i) + ", "+sql_start)
        if(i == 0):
            cursor.execute(sql_start)

        else:
            cursor.execute(sql)
            print(sql)

        # sql = "CREATE TABLE " + name + " ( " + input_ + " " + type_c + " COLLATE utf8_unicode_ci NOT NULL," \
        #       + " " + audit2 + " " + t2 + " COLLATE utf8_unicode_ci NOT NULL" + " )"
        # cursor.execute(sql)
    for str_name in get:
        print(str_name + "," + get[str_name])

    db.close()

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

