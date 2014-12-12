#!/usr/bin/env python
__author__ = 'alex'

import sys

sys.path.append('../../../target/generated-sources/thrift/py')

from tutorial import NotificationService

from thrift.transport import TSocket, TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer


class NotificationServiceHandler(NotificationService.Iface):
    def notify(self, value):
        print value


def start_python_server(port):
    print "Started client server"

    handler = NotificationServiceHandler()
    processor = NotificationService.Processor(handler)
    transport = TSocket.TServerSocket(port=port)
    tfactory = TTransport.TBufferedTransportFactory()
    pfactory = TBinaryProtocol.TBinaryProtocolFactory()

    server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)

    print "Starting python server..."
    server.serve()
    print "done!"


def get_port():
    return 12005


if __name__ == '__main__':
    start_python_server(get_port())
