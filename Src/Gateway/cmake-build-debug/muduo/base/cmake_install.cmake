# Install script for directory: /home/jcbjcbjc/Documents/test/muduo/base

# Set the install prefix
if(NOT DEFINED CMAKE_INSTALL_PREFIX)
  set(CMAKE_INSTALL_PREFIX "/usr/local")
endif()
string(REGEX REPLACE "/$" "" CMAKE_INSTALL_PREFIX "${CMAKE_INSTALL_PREFIX}")

# Set the install configuration name.
if(NOT DEFINED CMAKE_INSTALL_CONFIG_NAME)
  if(BUILD_TYPE)
    string(REGEX REPLACE "^[^A-Za-z0-9_]+" ""
           CMAKE_INSTALL_CONFIG_NAME "${BUILD_TYPE}")
  else()
    set(CMAKE_INSTALL_CONFIG_NAME "Debug")
  endif()
  message(STATUS "Install configuration: \"${CMAKE_INSTALL_CONFIG_NAME}\"")
endif()

# Set the component getting installed.
if(NOT CMAKE_INSTALL_COMPONENT)
  if(COMPONENT)
    message(STATUS "Install component: \"${COMPONENT}\"")
    set(CMAKE_INSTALL_COMPONENT "${COMPONENT}")
  else()
    set(CMAKE_INSTALL_COMPONENT)
  endif()
endif()

# Install shared libraries without execute permission?
if(NOT DEFINED CMAKE_INSTALL_SO_NO_EXE)
  set(CMAKE_INSTALL_SO_NO_EXE "0")
endif()

# Is this installation the result of a crosscompile?
if(NOT DEFINED CMAKE_CROSSCOMPILING)
  set(CMAKE_CROSSCOMPILING "FALSE")
endif()

# Set default install directory permissions.
if(NOT DEFINED CMAKE_OBJDUMP)
  set(CMAKE_OBJDUMP "/usr/bin/objdump")
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xUnspecifiedx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/lib" TYPE STATIC_LIBRARY FILES "/home/jcbjcbjc/Documents/test/cmake-build-debug/lib/libmuduo_base.a")
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xUnspecifiedx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/include/muduo/base" TYPE FILE FILES
    "/home/jcbjcbjc/Documents/test/muduo/base/AsyncLogging.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/Atomic.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/BlockingQueue.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/BoundedBlockingQueue.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/Condition.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/CountDownLatch.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/CurrentThread.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/Date.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/Exception.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/FileUtil.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/GzipFile.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/LogFile.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/LogStream.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/Logging.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/Mutex.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/ProcessInfo.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/Singleton.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/StringPiece.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/Thread.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/ThreadLocal.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/ThreadLocalSingleton.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/ThreadPool.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/TimeZone.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/Timestamp.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/Types.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/WeakCallback.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/copyable.h"
    "/home/jcbjcbjc/Documents/test/muduo/base/noncopyable.h"
    )
endif()

if(NOT CMAKE_INSTALL_LOCAL_ONLY)
  # Include the install script for each subdirectory.
  include("/home/jcbjcbjc/Documents/test/cmake-build-debug/muduo/base/tests/cmake_install.cmake")

endif()

