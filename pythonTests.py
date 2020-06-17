import requests
from urllib.parse import urljoin
import json
import http
from typing import Any, Dict, Optional

address = "localhost"
port = 8080
url = "http://"+address+":"+str(port)

# LISTA BŁĘDÓW : https://pl.wikipedia.org/wiki/Kod_odpowiedzi_HTTP
errorList = [[400, 'Bad Request'],[401, 'Unauthorized'],[402,'Payment Required'],
            [403, 'Forbidden'],[404,'Not Found'],[405,'Method Not Allowed'],
            [406,'Not Acceptable'],[407,'Proxy Authentication Required'],[408,'Request Timeout'],
            [409,'Conflict'],[410,'Gone'],[411,'Length required'],
            [412,'Precondition Failed'],[413,'Request Entity Too Large'],[414,'Request-URI Too Long'],
            [415,'Unsupported Media Type'],[416,'Requested Range Not Satisfiable'],[417,'Expectation Failed'],
            [418,'I’m a teapot'],[422,'Unprocessable entity'],[451,'Unavailable For Legal Reasons'],
            [500,'Internal Server Error'],[501,'Not Implemented'],[502,'Bad Gateway'],
            [503,'Service Unavaliable'],[504,'Gateway Timeout'],[505,'HTTP Version Not Supported'],
            [506,'Variant Also Negotiates'],[507,'Insufficient Storage'], [508,'Loop Detected'],
            [509,'Bandwitch Limit Exceeded'],[510,'Not Extended'],[511,'Network Authentication Required']]

def testConnection(address, port):
    import socket
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        return s.connect_ex((address, port)) == 0

def isError(response):
    for error in errorList:
        if(response.status_code==error[0]):
            return True
    return False

def printResponseInfo(response):
    print("\tStatus code: ", response.status_code)
    if(isError(response)==True):
        print("\tError:", response.json()['error'])
        print("\tMessage:", response.json()['message'], "\n")
    else:
        print("\tBody: ", response.content, "\n")

def testGetUsers():
    print("Testing getting users {http://localhost:8080/users}")
    response = requests.get("http://localhost:8080/users")
    printResponseInfo(response)
    
def testRegisterUser(username, password):
    print("Testing registering user {http://localhost:8080/sign-up}")
    data = {"username": username,"password": password}
    response = requests.post(url+"/sign-up", json=data)
    printResponseInfo(response)
    
def testLoginUser(username, password):
    print("Testing login user {http://localhost:8080/login}")
    data = {"username": username,"password": password}
    response = requests.post(url+"/login", json=data)
    printResponseInfo(response)
    token = response.headers
    return token
    
def testEditUser(username, oldPassword, newPassword, token):
    print("Testing editing user {http://localhost:8080/user/{username}/edit_account}")
    data = {"oldPassword":oldPassword,"newPassword":newPassword}
    response = requests.post(url+"/user/"+username+"/edit_account", json=data, headers=token)
    printResponseInfo(response)
    
def testAddCategory(username, name, description, token):
    print("Testing adding new category {http://localhost:8080/user/{username}/category}")
    data = {"name":name,"description":description}
    response = requests.post(url+"/user/"+username+"/category", json=data, headers=token)
    printResponseInfo(response)
    
def testGetCategories(username, token):
    print("Testing getting all categories {http://localhost:8080/user/{username}/category}")
    response = requests.get(url+"/user/"+username+"/category", headers=token)
    printResponseInfo(response)
    
def testEditCategory(username, categoryOldName, categoryNewName, categoryNewDescription ,token):
    print("Testing editing category {http://localhost:8080/user/{username}/category/{categoryName}}")
    data = {"name":categoryNewName,"description":categoryNewDescription}
    response = requests.put(url+"/user/"+username+"/category/"+categoryOldName, json=data, headers=token)
    printResponseInfo(response)
    
def testDeleteCategory(username, categoryName, token):
    print("Testing deleting category {http://localhost:8080/user/{username}/category/{categoryName}}")
    response = requests.delete(url+"/user/"+username+"/category/"+categoryName, headers=token)
    printResponseInfo(response)
    
def testAddTask(username, categoryName, taskName, taskDescription, 
        startDateTime,endDateTime,isActive, notification, token):
    print("Testing adding task {http://localhost:8080/user/{username}/category/{categoryName}/task}")
    data={"name":taskName,"description":taskDescription,"startDateTime":startDateTime,
        "endDateTime":endDateTime,"active":isActive,"notification":notification}
    response = requests.post("http://localhost:8080/user/"+username+"/category/"+categoryName+"/task", json=data, headers=token)
    printResponseInfo(response)
    
def getTasks(username, categoryName, token):
    print("Testing getting task {http://localhost:8080/user/{username}/category/{categoryName}/task}")
    response = requests.get(url+"/user/"+username+"/category/"+categoryName+"/task", headers=token)
    printResponseInfo(response)
    
def testEditTask(username, categoryName, taskId, taskName, taskDescription, 
        startDateTime,endDateTime,isActive, notification, token):
    print("Testing editing task {http://localhost:8080/user/{username}/category/{categoryName}/task/{taskId}}")
    data={"name":taskName,"description":taskDescription,"startDateTime":startDateTime,
        "endDateTime":endDateTime,"active":isActive,"notification":notification}
    response = requests.put(url+"/user/"+username+"/category/"+categoryName+"/task/"+str(taskId), json=data, headers=token)
    printResponseInfo(response)
    
def testDeleteTask(username, categoryName, taskId, token):
    print("Testing deleting task {http://localhost:8080/user/{username}/category/{categoryName}/task/{taskId}}")
    response = requests.delete(url+"/user/"+username+"/category/"+categoryName+"/task/"+str(taskId), headers=token)
    printResponseInfo(response)
    
for error in errorList:
    print("Error code",error[0],": ",error[1])

print("*******************************************************")

if(testConnection(address,port)==False):
    print("Error while connecting to server. Please check host and port")
else:
    testGetUsers()
    # Tests - sign-up
    testRegisterUser("", "testPassword")
    testRegisterUser("testUsername", "testPassword")
    testRegisterUser("testUsername", "testPassword")
    testRegisterUser("testUsername2", "testPassword2")
    token = testLoginUser("testUsername", "testPassword")
    testGetUsers()
    
    # Tests - edit account
    testEditUser("testUsername2", "testPassword", "testNewPassword", token)
    testEditUser("testUsername", "testPassword", "testNewPassword", token)
    
    # Tests - add category 
    testAddCategory("testUsername2", "Sport", "Sport activities", token)
    testAddCategory("testUsername", "Sport", "Sport activities", token)
    testAddCategory("testUsername", "Work", "Work at IT sector", token)
    testAddCategory("testUsername", "Fun", "Meetings with friends", token)
    testGetCategories("testUsername", token)
    
    # Tests - edit category
    testEditCategory("testUsername","Sport2","Sports","Sport (football + swimming pool)", token)
    testEditCategory("testUsername","Sport","Sports","Sport (football + swimming pool)", token)
    
    # Tests - delete category
    testDeleteCategory("testUsername","Fun2", token)
    testDeleteCategory("testUsername","Fun", token)
    testGetCategories("testUsername", token)
    
    # Tests - add task
    testAddTask("testUsername2","Sports","Swimming pool","Swimming pool with friends",
        "01/06/2020 12:00","01/06/2020 14:00",True,False, token)
    testAddTask("testUsername","Sports","Swimming pool","Swimming pool with friends",
        "01/06/2020 16:00","01/06/2020 14:00",True,False, token)
    testAddTask("testUsername","Sports","Swimming pool","Swimming pool with friends",
        "01/06/2020 12:00","01/06/2020 14:00",True,False, token)
    testAddTask("testUsername","Sports","Football","Football with friends",
        "02/06/2020 14:00","02/06/2020 16:00",True,False, token)
    testAddTask("testUsername","Sports","Badminkton","Badminkton with coach",
        "03/06/2020 14:00","03/06/2020 16:00",True,False, token)
    testAddTask("testUsername","Sports","Badminkton","Badminkton with coach",
        "03/06/2020 15:00","03/06/2020 17:00",True,False, token)
    getTasks("testUsername","Sports", token)
    
    # Tests - edit task 
    testEditTask("testUsername","Sports2", 2, "Football", "Football with team",
        "01/06/2020 15:00","01/06/2020 18:00",True,False, token)
    testEditTask("testUsername","Sports", 2, "Football", "Football with team",
        "01/06/2020 15:00","01/06/2020 18:00",True,False, token)
        
    # Tests - delete task
    testDeleteTask("testUsername","Sports2", 3, token)
    testDeleteTask("testUsername","Sports", 3, token)
    getTasks("testUsername","Sports", token)

print("*******************************************************")








