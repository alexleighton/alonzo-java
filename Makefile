################################################################################
#   Variables                                                                  #
################################################################################

ROOT = $(realpath $(dir $(lastword $(MAKEFILE_LIST))))

DESTDIR = $(ROOT)/../alonzo-java


################################################################################
#   Build Steps                                                                #
################################################################################

.PHONY: all build clean serve

all: build

clean:
	@rm -r $(ROOT)/_site/

build:
	@cd $(ROOT); jekyll build --destination $(DESTDIR)

serve:
	@cd $(ROOT); ./jekyll-serve.sh
