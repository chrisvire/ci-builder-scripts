#!/bin/bash

stderr()
{
	echo >&2 "$PROGRAM_NAME: $1"
}

init()
{
	# Process arguments.
	while (( $# )); do
		case $1 in
			# Process individual arguments here. Use shift and $1 to get an argument value.
			# Example: -d) DEBUG=true ;;
			# Example: --outfile) shift; OUTFILE=$1 ;;
			# Example: *) echo "Unexpected argument: $1"; exit 1 ;;
			--debkeyid) shift; debkeyid=$1 ;;
			# Space-delimited list of releases. eg "precise raring"
			--dists) shift; dists_arg=$1 ;;
			# Space-delimited list of architectures. eg "amd64 i386"
			--arches) shift; arches_arg=$1 ;;
			# Comma-delimited list of non-default repositorydir=committish mapping (hash, tag, branch). eg "fwrepo/fw=2568e4f,fwrepo/fw/Localizations=linux/FieldWorks8.0.3-beta4,fwrepo/fw/DistFiles/Helps=origin/master"
			--repository-committishes) shift; repository_committishes_arg=$1 ;;
			# Don't upload packages at the end
			--simulate-dput) dput_simulate="-s" ;;
			--package-version-extension) shift; package_version_extension=$1 ;;
			--package-name-suffix) shift; package_name_suffix=$1 ;;
			--main-package-name) shift; main_package_name_arg=$1 ;;
			# Skip cleaning and updating local repository
			--preserve-repository) preserve_repository_arg=true ;;
			# Space-delimited list of binary packages to remove from debian/control file before creating source package
			--omit-binary-packages) shift; omit_binary_packages_arg=$1 ;;
			# For making release packages. Do not add a new entry to the changelog. Package versions will be as specified in the last changelog entry, without a nightly timestamp appended.
			--preserve-changelog) preserve_changelog_arg=true ;;
			# Normally source and binary packages end up getting deleted during future runs of this script. If the source and binary packages from this run are to be kept around so they can be manually processed (eg to sign or upload to a different location), then this argument can be used.
			--preserve-products) preserve_products_arg=true ;;
			# Package repository to upload the resulting binary packages to. "llso-main" or "llso-experimental".
			--destination-repository) shift; destination_repository_arg=$1 ;;
			# The distros we might possibly want to build
			--supported-distros) shift; supported_distros_arg=$1 ;;
			*) stderr "Error: Unexpected argument \"$1\". Exiting." ; exit 1 ;;
		esac
		shift || (stderr "Error: The last argument is missing a value. Exiting."; false) || exit 2
	done

	DISTRIBUTIONS_TO_PACKAGE="${dists_arg:-precise}"
	DISTS_TO_PROCESS="${supported_distros_arg:-precise trusty wheezy saucy}"
	ARCHES_TO_PACKAGE="${arches_arg:-i386 amd64}"
	ARCHES_TO_PROCESS="amd64 i386"
	PACKAGING_ROOT="$HOME/packages"

	# set Debian/changelog environment
	export DEBFULLNAME="${main_package_name_arg:-Unknown} Package Signing Key"
	export DEBEMAIL='jenkins@sil.org'

	package_name_suffix=""
	repo_base_dir=${WORKSPACE:-$PACKAGING_ROOT/$main_package_name_arg}
	pbuilder_path="$HOME/pbuilder"
	debian_path="debian"
	source_package_name=$(dpkg-parsechangelog |grep ^Source:|cut -d' ' -f2)

	if [ -d ".hg" ]; then
		VCS=hg
	else
		VCS=git
	fi

}
