##### Build artifact and Docker image:
* run ``mvn clean package``


### How to obtain authorization token and login user?

###1. Send POST:
```
Request:
http://domain:port/auth/login

Body (JSON):
{
"username":"emailinapp@mail.com",
"password":"TestPassword@1"
}

```

###2. Token should be accessible from header and body with user info:
```
Header:
Authorization: 

Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiZW9udGltZXRlc3RAZ21haWwuY29tIiwicm9sZXMiOltdLCJlbWFpbCI6ImJlb250aW1ldGVzdEBnbWFpbC5jb20iLCJndWlkIjoiMTdkN2UzNGEtYzZiOC00NWNhLWJjYjMtY2FmZDAyZjMzMjUxIiwiaWF0IjoxNTUyMzkxMTA1LCJleHAiOjE1NTIzOTIzMDV9.LJx-bfiQ903KINaXWBVbvaLN8LZJma9qz8i5zoX3luMVRynIf5ahw95-ouuVJ4hfca8rukNOfnGes16c1GzCvw


Body:

{
    "userId": "17d7e22a-c6b8-45ca-bcb3-cafd02f33251",
    "email": "emailinapp@mail.com",
    "firstName": "UserName",
    "lastName": "UserLastName",
    "active": true,
    "department": "ExampleDepartment",
    "roles": []
}
```


###3. If you obtain token - user is successfully logged in.
```
REMEMBER TO ADD AUTHORIZATION TOKEN TO EVERY NEXT REQUEST!
```

