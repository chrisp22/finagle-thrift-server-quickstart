/**
 * Generated by Scrooge
 *   version: 3.14.1
 *   rev: a996c1128a032845c508102d62e65fc0aa7a5f41
 *   built at: 20140501-114733
 */
package com.twitter.finagle.example.thriftscala

import com.twitter.finagle.{Service => FinagleService}
import com.twitter.scrooge.{ThriftStruct, TReusableMemoryTransport}
import com.twitter.util.Future
import java.nio.ByteBuffer
import java.util.Arrays
import org.apache.thrift.protocol._
import org.apache.thrift.TApplicationException
import org.apache.thrift.transport.TMemoryInputTransport
import scala.collection.mutable.{
  ArrayBuffer => mutable$ArrayBuffer, HashMap => mutable$HashMap}
import scala.collection.{Map, Set}


@javax.annotation.Generated(value = Array("com.twitter.scrooge.Compiler"))
class Hello$FinagleService(
  iface: Hello[Future],
  protocolFactory: TProtocolFactory
) extends FinagleService[Array[Byte], Array[Byte]] {
  import Hello._

  private[this] val tlReusableBuffer = new ThreadLocal[TReusableMemoryTransport] {
    override def initialValue() = TReusableMemoryTransport(512)
  }

  private[this] def reusableBuffer: TReusableMemoryTransport = {
    val buf = tlReusableBuffer.get()
    buf.reset()
    buf
  }

  private[this] def resetBuffer(trans: TReusableMemoryTransport, maxCapacity: Int = 4096) {
    if (trans.currentCapacity > maxCapacity) {
      tlReusableBuffer.remove()
    }
  }

  protected val functionMap = new mutable$HashMap[String, (TProtocol, Int) => Future[Array[Byte]]]()

  protected def addFunction(name: String, f: (TProtocol, Int) => Future[Array[Byte]]) {
    functionMap(name) = f
  }

  protected def exception(name: String, seqid: Int, code: Int, message: String): Future[Array[Byte]] = {
    try {
      val x = new TApplicationException(code, message)
      val memoryBuffer = reusableBuffer
      try {
        val oprot = protocolFactory.getProtocol(memoryBuffer)

        oprot.writeMessageBegin(new TMessage(name, TMessageType.EXCEPTION, seqid))
        x.write(oprot)
        oprot.writeMessageEnd()
        oprot.getTransport().flush()
        Future.value(Arrays.copyOfRange(memoryBuffer.getArray(), 0, memoryBuffer.length()))
      } finally {
        resetBuffer(memoryBuffer)
      }
    } catch {
      case e: Exception => Future.exception(e)
    }
  }

  protected def reply(name: String, seqid: Int, result: ThriftStruct): Future[Array[Byte]] = {
    try {
      val memoryBuffer = reusableBuffer
      try {
        val oprot = protocolFactory.getProtocol(memoryBuffer)

        oprot.writeMessageBegin(new TMessage(name, TMessageType.REPLY, seqid))
        result.write(oprot)
        oprot.writeMessageEnd()

        Future.value(Arrays.copyOfRange(memoryBuffer.getArray(), 0, memoryBuffer.length()))
      } finally {
        resetBuffer(memoryBuffer)
      }
    } catch {
      case e: Exception => Future.exception(e)
    }
  }

  final def apply(request: Array[Byte]): Future[Array[Byte]] = {
    val inputTransport = new TMemoryInputTransport(request)
    val iprot = protocolFactory.getProtocol(inputTransport)

    try {
      val msg = iprot.readMessageBegin()
      val func = functionMap.get(msg.name)
      func match {
        case Some(fn) => 
          fn(iprot, msg.seqid)
        case _ =>
          TProtocolUtil.skip(iprot, TType.STRUCT)
          exception(msg.name, msg.seqid, TApplicationException.UNKNOWN_METHOD,
            "Invalid method name: '" + msg.name + "'")
      }      
    } catch {
      case e: Exception => Future.exception(e)
    }
  }

  // ---- end boilerplate.

  addFunction("hi", { (iprot: TProtocol, seqid: Int) =>
    try {
      val args = hi$args.decode(iprot)
      iprot.readMessageEnd()
      (try {
        iface.hi()
      } catch {
        case e: Exception => Future.exception(e)
      }) flatMap { value: String =>
        reply("hi", seqid, hi$result(success = Some(value)))
      } rescue {
        case e => Future.exception(e)
      }
    } catch {
      case e: TProtocolException => {
        iprot.readMessageEnd()
        exception("hi", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage)
      }
      case e: Exception => Future.exception(e)
    }
  })
}