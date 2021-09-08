cmake_minimum_required(VERSION 3.0.0)
project(sample VERSION 0.1.0)

include(CTest)
enable_testing()

set (SRC_FILES main.cpp ${CMAKE_CURRENT_SOURCE_DIR}/src/bst.cpp)

add_executable(sample ${SRC_FILES})

target_include_directories(sample PUBLIC ${CMAKE_CURRENT_SOURCE_DIR}/include)

set_target_properties(sample PROPERTIES COMPILE_FLAGS "-save-temps")

set(CPACK_PROJECT_NAME ${PROJECT_NAME})
set(CPACK_PROJECT_VERSION ${PROJECT_VERSION})
include(CPack)
