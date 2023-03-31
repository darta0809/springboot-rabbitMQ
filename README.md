# springboot-rabbitMQ

rabbitMQ  
http://localhost:15672/

## Exchange

### Direct

當消息被發送到Direct Exchange時，它會根據設置的routing key來判斷將消息路由到哪些Queue中。  
當一個Queue被綁定到Direct Exchange上時，可以指定一個或多個routing key，用來指定該Queue要接收哪些消息。  
Direct Exchange非常適合用於點對點的通信場景，以及需要根據特定條件來過濾消息的場景。

### Topic

在Topic Exchange的工作模式中，Exchange會根據routing key和binding key的匹配程度，將消息路由到對應的Queue中。  
其中，binding key可以包含通配符符號"*"和"#"，用來表示匹配任意一個單詞或匹配任意多個單詞。  
通過這種方式，可以實現更加靈活的消息路由和分發，適用於複雜的分佈式系統場景。

### Fanout

當消息被發送到Fanout Exchange時，Exchange會將消息發送到所有與之綁定的Queue中。  
不需要routing key或者binding key，因為Fanout Exchange會忽略這些信息，直接將消息發送到所有綁定的Queue中。
這種方式非常適合用於一些需要將消息發送給所有接收方的場景，比如日誌系統。

### Queue

* Direct
* Topic 1 and 2
* Fanout 1 and 2

## ACK 機制

當消費者（Consumer）從RabbitMQ服務器上獲取（consume）消息後，RabbitMQ會等待消費者發送ACK確認，表示消息已被成功接收和處理。  
如果RabbitMQ沒有收到ACK確認，就會認為消息沒有被正確處理，並且將其重新分發到其它消費者。  
這樣可以保證消息不會因為某個消費者的失敗而被丟失，同時也可以保證消息的可靠傳遞。

ACK機制可以分為兩種模式：自動ACK模式和手動ACK模式。

### 自動ACK模式

消費者在接收到消息後，RabbitMQ會自動發送ACK確認，表示消息已被成功處理。這種模式下，消費者不需要額外的代碼來發送ACK確認。

### 手動ACK模式

消費者需要顯式地調用channel.basicAck方法來發送ACK確認，以表示消息已被成功處理。

