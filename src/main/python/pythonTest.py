#!/usr/bin/env python
__author__ = 'alex'

import sys
sys.path.append('../../../target/generated-sources/thrift/py')

from tutorial import MultiplicationService
from tutorial import NotificationService
from tutorial.ttypes import *

from thrift import Thrift
from thrift.transport import TSocket, TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer, TNonblockingServer
from multiprocessing import Process
from time import sleep

try:
    # Make socket
    transport = TSocket.TSocket('localhost', 12005)

    # Buffering is critical. Raw sockets are very slow
    transport = TTransport.TBufferedTransport(transport)

    # Wrap in a protocol
    protocol = TBinaryProtocol.TBinaryProtocol(transport)

    # Create a client to use the protocol encoder
    client = NotificationService.Client(protocol)

    # Connect!
    transport.open()

    msg = client.notify("Hello from python")

    transport.close()

except Thrift.TException, tx:
    print "%s" % (tx.message)