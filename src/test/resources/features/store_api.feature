Feature: Pruebas del API de Store de PetStore

  Scenario Outline: Creación y consulta de una orden en la tienda de mascotas
    Given que tengo acceso al API de PetStore
    When creo una nueva orden con los siguientes datos:
      | id | petId | quantity | shipDate | status | complete |
      | <id> | <petId> | <quantity> | <shipDate> | <status> | <complete> |
    Then el código de respuesta debe ser 200
    And el cuerpo de la respuesta debe contener los datos de la orden creada
    And la orden debe estar almacenada correctamente en la base de datos

    When consulto la orden con id <id>
    Then el código de respuesta debe ser 200
    And el cuerpo de la respuesta debe contener los siguientes datos:
      | id | petId | quantity | shipDate | status | complete |
      | <id> | <petId> | <quantity> | <shipDate> | <status> | <complete> |

    Examples:
      | id | petId | quantity | shipDate | status | complete |
      | 5 | 25 | 2 | 2024-09-28T10:00:00Z | placed | false |
      | 6 | 26 | 1 | 2024-09-29T14:30:00Z | approved | true |