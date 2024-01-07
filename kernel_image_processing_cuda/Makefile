#
# Macros
#
IMG_LDFLAG	= -lpng
LDFLAGS 	= $(IMG_LDFLAG) -lm

CC		= /usr/local/cuda/bin/nvcc	
CFLAGS		=   -gencode=arch=compute_52,code=sm_52 \
  -gencode=arch=compute_60,code=sm_60 \
  -gencode=arch=compute_61,code=sm_61 \
  -gencode=arch=compute_70,code=sm_70 \
  -gencode=arch=compute_75,code=sm_75 \
  -gencode=arch=compute_75,code=compute_75 \
		  -gencode arch=compute_53,code=sm_53 \
		  -gencode arch=compute_61,code=sm_61 \
		  -gencode arch=compute_52,code=sm_52 \
		  -gencode arch=compute_50,code=sm_50 \

		  --fmad=false \
		  -O2 -std=c++11 \
		  --compiler-options -Wall

CPP_SRCS	= kernel.cpp \
		  image.cpp 

CPP_HDRS	= kernel.h \
		  image.h \
		  gpu_convolution.h 

CU_SRCS		= main.cu \
		  gpu_convolution.cu

CPP_OBJS	= $(CPP_SRCS:.cpp=.o) 
CU_OBJS		= $(CU_SRCS:.cu=.o)
TARGET		= kernel_convolution

CPP_DEPS	= $(CPP_SRCS:.cpp=.d)
CU_DEPS		= $(CU_SRCS:.cu=.d)
DEP_FILE	= Makefile.dep

#
# Suffix rules
#
.SUFFIXES: .cpp
.cpp.o:
	$(CC) $(CFLAGS)  -c $<

.SUFFIXES: .cu
.cu.o:
	$(CC) $(CFLAGS)  -c $<

.SUFFIXES: .d
.cpp.d:
	$(CC) -M $< > $*.d
.cu.d:
	$(CC) -M $< > $*.d

#
# Generating the target
#
all: $(DEP_FILE) $(TARGET) 

#
# Linking the execution file
#
$(TARGET) : $(CU_OBJS) $(CPP_OBJS) 
	$(CC) -o $@ $(CU_OBJS) $(CPP_OBJS) $(LDFLAGS)

#
# Generating and including dependencies
#
depend: $(DEP_FILE)
$(DEP_FILE) : $(CPP_DEPS) $(CU_DEPS)
	cat $(CPP_DEPS) $(CU_DEPS) > $(DEP_FILE)
ifeq ($(wildcard $(DEP_FILE)),$(DEP_FILE))
include $(DEP_FILE)
endif

#
# Cleaning the files
#
clean:
	rm -f $(CU_OBJS) $(CPP_OBJS) $(CPP_DEPS) $(CU_DEPS) $(DEP_FILE) $(TARGET) *~
