*** Settings ***
Library         String
Library         REST    http://localhost:8080/todo-service  ssl_verify=false

*** Keywords ***
Set expectations
    Expect response   { "status": { "enum": [200, 201, 204, 400] } }
    Expect response   { "seconds": { "maximum": 2} }

*** Test Cases ***
GET an existing todo
    GET             /api/todos/67
    Output          response    body
    Integer         response    body    id          67
    String          response    body    description "aaaaaaaaaaaeeeeeeeeeeeeeeeeeeeaaaaa"
    Boolean         response    body    finished    false
    Integer         response    body    created     1661540702081









