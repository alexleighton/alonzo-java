################################################################################
#   Variables                                                                  #
################################################################################

PROJECT_NAME=alonzo-java

BASE_PACKAGE_NAME=alonzo

################################################################################

ROOT = $(realpath $(dir $(lastword $(MAKEFILE_LIST))))


############################################################
# Build Output Directories                                 #
############################################################

BUILDIR = $(ROOT)/build


############################################################
# Main Artifact Building                                   #
############################################################

SRCDIR = $(ROOT)/src

ARTIFACTDIR = $(BUILDIR)/src
ARTIFACT_SRCS_FILE = $(ARTIFACTDIR)/sources
ARTIFACT_MANIFEST = $(ARTIFACTDIR)/Manifest

ARTIFACT_MAIN_CLASS = $(BASE_PACKAGE_NAME).Main
ARTIFACT_JAR = $(PROJECT_NAME).jar


############################################################
# Test Artifact Building                                   #
############################################################

TSTDIR = $(ROOT)/tst

TESTINGDIR = $(BUILDIR)/test
TESTING_SRCS_FILE = $(TESTINGDIR)/sources

TESTING_MAIN_CLASS = $(BASE_PACKAGE_NAME).TestingMain



################################################################################
#   Build Steps                                                                #
################################################################################

.PHONY: all clean build build-test test

all: test

clean:
	@rm -r "$(BUILDIR)"

build:
	@mkdir -p "$(ARTIFACTDIR)"

	@find "$(SRCDIR)" -type f -name "*.java" > "$(ARTIFACT_SRCS_FILE)"

	@javac -d "$(ARTIFACTDIR)" -target 1.8 -source 1.8 -cp '$(ARTIFACTDIR)/*' "@$(ARTIFACT_SRCS_FILE)"

	@printf '%s\n%s\n\n' "Main-Class: $(ARTIFACT_MAIN_CLASS)" > "$(ARTIFACT_MANIFEST)"
	@jar cvmf "$(ARTIFACT_MANIFEST)" "$(ARTIFACTDIR)/$(ARTIFACT_JAR)" -C "$(ARTIFACTDIR)/" "$(BASE_PACKAGE_NAME)" > /dev/null
	@mv "$(ARTIFACTDIR)/$(ARTIFACT_JAR)" "$(BUILDIR)"

run: build
	@java -jar "$(BUILDIR)/$(ARTIFACT_JAR)"

build-test: build
	@mkdir -p "$(TESTINGDIR)"
	@cd "$(BUILDIR)"; cp "$(ARTIFACT_JAR)" "$(TESTINGDIR)"

	@find "$(TSTDIR)" -type f -name "*.java" > "$(TESTING_SRCS_FILE)"

	@javac -d "$(TESTINGDIR)" -target 1.8 -source 1.8 "@$(TESTING_SRCS_FILE)" -classpath "$(TESTINGDIR)/$(ARTIFACT_JAR)"

test: build-test
	@java -cp "$(TESTINGDIR):$(TESTINGDIR)/*" "$(TESTING_MAIN_CLASS)"
