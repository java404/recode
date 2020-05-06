all: publish

pkg:
	bash ./build_pkg.sh

rpm: pkg
	bash ./build_rpm.sh

publish: rpm
	bash ./build_publish.sh

clean:
	bash ./build_clean.sh
