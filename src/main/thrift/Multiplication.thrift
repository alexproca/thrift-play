namespace java tutorial
namespace py tutorial

typedef i32 int

struct RegisteringClient {

    1:int port
    2:string name

}

service NotificationService
{
    oneway void notify(1:string value);
}

service MultiplicationService
{
    int multiply(1:int n1, 2:int n2)
    void registerClient(1:RegisteringClient registeringClient)
}