import io
import json
import qrcode
import xlwt as xlwt
from django.shortcuts import render, render_to_response
from django.http import HttpResponse, StreamingHttpResponse
import pymysql
from django.views.decorators.csrf import csrf_exempt

path = "/home/PycharmProjects/PyWeb/images"


def home(request):
    return render_to_response("home.html")


def display(request):
    db = pymysql.connect("localhost", "root", "root", "audit")
    cursor = db.cursor()

    sql_count = "SELECT count(*) FROM information_schema.tables WHERE table_schema='audit'"
    cursor.execute(sql_count)
    count = cursor.fetchone()
    sql_tables = "show tables"
    cursor.execute(sql_tables)
    data = cursor.fetchall()
    print(data)
    name = []
    for items, table in enumerate(data):
        if(items < count[0] - 11):
            name.append(table[0])
        else:
            break
    print(name)

    # cursor.execute(sql)
    # result = cursor.fetchall()

    response = HttpResponse(content_type='application/vnd.ms-excel')  # 指定返回为excel文件
    response['Content-Disposition'] = 'attachment;filename=类别.xls'  # 指定返回文件名
    wb = xlwt.Workbook(encoding='utf-8')  # 设定编码类型为utf8
    for table_name in name:
        sheet = wb.add_sheet(table_name)  # excel里添加类别

        sql_item = "DESC " + table_name
        cursor.execute(sql_item)
        desc = cursor.fetchall()
        print(desc)
        item = []
        for num, items in enumerate(desc):
            print(items[0])
            sheet.write(0, num, items[0])
            sql_data = "SELECT `" + items[0] + "` FROM `" + table_name + "`"
            cursor.execute(sql_data)
            result = cursor.fetchall()
            print(result)
            row = 1
            for list in result:
                sheet.write(row, num, list[0])
                row += 1
            # item.append(items[0])

        # sheet.write(0, 0, 'id')
        # sheet.write(0, 1, '名字')

    # row = 1
    # for list in result:
    #     sheet.write(row, 0, list['id'])
    #     sheet.write(row, 1, list['label'])
    #     row = row + 1

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
    sql_start = "CREATE TABLE `" + name + "` ( 日期 CHAR(20) COLLATE utf8_unicode_ci NOT NULL, 时间 CHAR(20) COLLATE utf8_unicode_ci NOT NULL )"
    cursor.execute(sql_start)
    print(sql_start)
    global filename
    filename = name

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
        # sql_start = "CREATE TABLE " + name + " ( " + input_ + " " + type_c + " COLLATE utf8_unicode_ci NOT NULL" + " )"
        sql = "ALTER TABLE `" + name + "` ADD COLUMN `" + input_ + "` " + type_c + " COLLATE utf8_unicode_ci NOT NULL"

        cursor.execute(sql)
        print(sql)

        # sql = "CREATE TABLE " + name + " ( " + input_ + " " + type_c + " COLLATE utf8_unicode_ci NOT NULL," \
        #       + " " + audit2 + " " + t2 + " COLLATE utf8_unicode_ci NOT NULL" + " )"
        # cursor.execute(sql)
    for str_name in get:
        print(str_name + "," + get[str_name])

    db.close()

    generate(request, name)

    return render_to_response("submit.html")


def generate(request, name):
    message = '{"name":"%s"}' % name
    qr = qrcode.QRCode(version=3,
                       error_correction=qrcode.ERROR_CORRECT_M,
                       box_size=8,
                       border=4,
                       )
    qr.add_data(message)
    qr.make(fit=True)
    img = qr.make_image()
    img = img.convert("RGBA")

    # if logo and os.path.exists(logo):
    #     icon = Image.open(logo)
    #     img_w, img_h = img.size
    #     factor = 4
    #     size_w = int(img_w / factor)
    #     size_h = int(img_h / factor)
    #
    #     icon_w, icon_h = icon.size
    #     if icon_w > size_w:
    #         icon_w = size_w
    #     if icon_h > size_h:
    #         icon_h = size_h
    #     icon = icon.resize((icon_w, icon_h), Image.ANTIALIAS)
    #
    #     w = int((img_w - icon_w) / 2)
    #     h = int((img_h - icon_h) / 2)
    #     icon = icon.convert("RGBA")
    #     img.paste(icon, (w, h), icon)

    img.save(path + "/%s.png" % name)


def file_download(request):
    def file_iterator(file_name, chunk_size=512):
        with open(file_name, 'rb') as f:  # 如果不加‘rb’以二进制方式打开，文件流中遇到特殊字符会终止下载，下载下来的文件不完整
            while True:
                c = f.read(chunk_size)
                if c:
                    yield c
                else:
                    break

    filepath = path + "/%s.png" % filename
    response = StreamingHttpResponse(file_iterator(filepath))
    response['Content-Type'] = 'image/png'
    fullname = "%s.png" % filename
    print(fullname)
    response['Content-Disposition'] = 'attachment;filename=%s' % fullname  # 此处kwargs['fname']是要下载的文件的文件名称
    return response


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
    sql3 = "DESC `" + item_name + "`"
    print(sql3)
    cursor.execute(sql3)
    desc = cursor.fetchall()
    print(desc)
    item = []
    type = []
    for i in desc:
        print(i[0] + "+" + i[1])
        if((i[0] != "日期") and (i[0] != "时间")):
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
            key += "`" + itemName[i][j] + "`"
            value += "'" + dataArray[i][j] + "'"
            if j < len(itemName[i]) - 1:
                key += ","
                value += ","
        sql_in = "INSERT INTO `" + tableName[i] + "` (" + key + ") VALUES (" + value + ")"
        print(sql_in)
        cursor.execute(sql_in)
        db.commit()

