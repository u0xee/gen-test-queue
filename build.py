#!/usr/bin/env python3

import argparse
import os


from build_functions import *

src_dir = 'src'
starter_dir = 'starter'
doc_dir = 'doc'
resources_dir = 'resources'
archive_dir = 'archive'
javadoc_dir = os.path.join(doc_dir, 'javadoc')
course_name = 'CSU CS165'


def build_project(args):
    print('==== Creating starter source')
    generate_starter_src(src_dir, starter_dir + "/src")

    print('==== Creating javadocs')
    create_javadocs = javadocs_command(src_dir, javadoc_dir, course_name)
    subprocess.run(create_javadocs)
    print('==== Creating html from asciidoc')
    generate_html_from_asciidoc(doc_dir)

    print('==== Creating starter jar')
    create_starter_jar(archive_dir, create_starter_jar_name(), resources_dir, starter_dir)

    print('==== Opening permissions to doc and archive')
    open_permissions_recursively(doc_dir)
    open_permissions_recursively(archive_dir)

    print('==== Closing permissions to source')
    close_permissions_at_dir(src_dir)

    
# Main parser
parser = argparse.ArgumentParser(description='Scaffolding to support school assignment development and deployment.')
parser.set_defaults(func=lambda args: parser.print_usage())
subparsers = parser.add_subparsers(help='action to perform')

# Tar subparser
tar_parser = subparsers.add_parser('tar', description='Package project up in a bzipped tarball',
                                   help='Package project up (-h for options)')
tar_parser.set_defaults(func=create_tarball)
default_tar_filename = os.path.basename(os.getcwd()) + '.tbz'
tar_parser.add_argument('-f', '--filename',
                        default=default_tar_filename,
                        help='name for the output tar file')

# Build subparser
build_parser = subparsers.add_parser('build', description='Build project by generating docs, starter source, and changing permissions',
                                     help='Build project, ready it for deployment (-h for options)')
build_parser.set_defaults(func=build_project)


# Parse and dispatch
args = parser.parse_args()
args.func(args)
