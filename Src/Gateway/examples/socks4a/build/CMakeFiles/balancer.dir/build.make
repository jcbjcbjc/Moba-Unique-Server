# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.24

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

# Disable VCS-based implicit rules.
% : %,v

# Disable VCS-based implicit rules.
% : RCS/%

# Disable VCS-based implicit rules.
% : RCS/%,v

# Disable VCS-based implicit rules.
% : SCCS/s.%

# Disable VCS-based implicit rules.
% : s.%

.SUFFIXES: .hpux_make_needs_suffix_list

# Command-line flag to silence nested $(MAKE).
$(VERBOSE)MAKESILENT = -s

#Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E rm -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/jcbjcbjc/Documents/muduo/examples/socks4a

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/jcbjcbjc/Documents/muduo/examples/socks4a/build

# Include any dependencies generated for this target.
include CMakeFiles/balancer.dir/depend.make
# Include any dependencies generated by the compiler for this target.
include CMakeFiles/balancer.dir/compiler_depend.make

# Include the progress variables for this target.
include CMakeFiles/balancer.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/balancer.dir/flags.make

CMakeFiles/balancer.dir/balancer.cc.o: CMakeFiles/balancer.dir/flags.make
CMakeFiles/balancer.dir/balancer.cc.o: /home/jcbjcbjc/Documents/muduo/examples/socks4a/balancer.cc
CMakeFiles/balancer.dir/balancer.cc.o: CMakeFiles/balancer.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/jcbjcbjc/Documents/muduo/examples/socks4a/build/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object CMakeFiles/balancer.dir/balancer.cc.o"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -MD -MT CMakeFiles/balancer.dir/balancer.cc.o -MF CMakeFiles/balancer.dir/balancer.cc.o.d -o CMakeFiles/balancer.dir/balancer.cc.o -c /home/jcbjcbjc/Documents/muduo/examples/socks4a/balancer.cc

CMakeFiles/balancer.dir/balancer.cc.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/balancer.dir/balancer.cc.i"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /home/jcbjcbjc/Documents/muduo/examples/socks4a/balancer.cc > CMakeFiles/balancer.dir/balancer.cc.i

CMakeFiles/balancer.dir/balancer.cc.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/balancer.dir/balancer.cc.s"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /home/jcbjcbjc/Documents/muduo/examples/socks4a/balancer.cc -o CMakeFiles/balancer.dir/balancer.cc.s

# Object files for target balancer
balancer_OBJECTS = \
"CMakeFiles/balancer.dir/balancer.cc.o"

# External object files for target balancer
balancer_EXTERNAL_OBJECTS =

balancer: CMakeFiles/balancer.dir/balancer.cc.o
balancer: CMakeFiles/balancer.dir/build.make
balancer: CMakeFiles/balancer.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/home/jcbjcbjc/Documents/muduo/examples/socks4a/build/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking CXX executable balancer"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/balancer.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/balancer.dir/build: balancer
.PHONY : CMakeFiles/balancer.dir/build

CMakeFiles/balancer.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/balancer.dir/cmake_clean.cmake
.PHONY : CMakeFiles/balancer.dir/clean

CMakeFiles/balancer.dir/depend:
	cd /home/jcbjcbjc/Documents/muduo/examples/socks4a/build && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/jcbjcbjc/Documents/muduo/examples/socks4a /home/jcbjcbjc/Documents/muduo/examples/socks4a /home/jcbjcbjc/Documents/muduo/examples/socks4a/build /home/jcbjcbjc/Documents/muduo/examples/socks4a/build /home/jcbjcbjc/Documents/muduo/examples/socks4a/build/CMakeFiles/balancer.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/balancer.dir/depend

