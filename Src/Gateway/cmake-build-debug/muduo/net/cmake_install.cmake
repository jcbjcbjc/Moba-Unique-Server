# Install script for directory: /home/jcbjcbjc/Documents/test/muduo/net

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
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/lib" TYPE STATIC_LIBRARY FILES "/home/jcbjcbjc/Documents/test/cmake-build-debug/lib/libmuduo_net.a")
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xUnspecifiedx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/include/muduo/net" TYPE FILE FILES
    "/home/jcbjcbjc/Documents/test/muduo/net/Buffer.h"
    "/home/jcbjcbjc/Documents/test/muduo/net/Callbacks.h"
    "/home/jcbjcbjc/Documents/test/muduo/net/Channel.h"
    "/home/jcbjcbjc/Documents/test/muduo/net/Endian.h"
    "/home/jcbjcbjc/Documents/test/muduo/net/EventLoop.h"
    "/home/jcbjcbjc/Documents/test/muduo/net/EventLoopThread.h"
    "/home/jcbjcbjc/Documents/test/muduo/net/EventLoopThreadPool.h"
    "/home/jcbjcbjc/Documents/test/muduo/net/InetAddress.h"
    "/home/jcbjcbjc/Documents/test/muduo/net/TcpClient.h"
    "/home/jcbjcbjc/Documents/test/muduo/net/TcpConnection.h"
    "/home/jcbjcbjc/Documents/test/muduo/net/TcpServer.h"
    "/home/jcbjcbjc/Documents/test/muduo/net/TimerId.h"
    )
endif()

if(NOT CMAKE_INSTALL_LOCAL_ONLY)
  # Include the install script for each subdirectory.
  include("/home/jcbjcbjc/Documents/test/cmake-build-debug/muduo/net/http/cmake_install.cmake")
  include("/home/jcbjcbjc/Documents/test/cmake-build-debug/muduo/net/inspect/cmake_install.cmake")
  include("/home/jcbjcbjc/Documents/test/cmake-build-debug/muduo/net/tests/cmake_install.cmake")
  include("/home/jcbjcbjc/Documents/test/cmake-build-debug/muduo/net/protobuf/cmake_install.cmake")
  include("/home/jcbjcbjc/Documents/test/cmake-build-debug/muduo/net/protorpc/cmake_install.cmake")

endif()

