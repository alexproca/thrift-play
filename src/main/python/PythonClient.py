#!/usr/bin/env python
__author__ = 'alex'

import sys

sys.path.append('../../../target/generated-sources/thrift/py')

from tutorial import MultiplicationService
from tutorial.ttypes import *

from thrift import Thrift
from thrift.transport import TSocket, TTransport
from thrift.protocol import TBinaryProtocol
from time import sleep


def start():
    try:
        # Make socket
        transport = TSocket.TSocket('localhost', 9090)

        # Buffering is critical. Raw sockets are very slow
        transport = TTransport.TFramedTransport(transport)

        # Wrap in a protocol
        protocol = TBinaryProtocol.TBinaryProtocol(transport)

        # Create a client to use the protocol encoder
        client = MultiplicationService.Client(protocol)

        # Connect!
        transport.open()

        # p = Process(target=add_some_handlers, args=(get_port(),))
        # p.start()

        # print p

        client.registerClient(RegisteringClient(port=12005, name='ClientPython'))

        for i in range(100):
            product = client.multiply(4, 5)
            print '4*5=%d' % (product)
            sleep(1)

        # Close!
        transport.close()


    except Thrift.TException, tx:
        print '%s' % (tx.message)
    except KeyboardInterrupt:
        pass


if __name__ == '__main__':
    start()
