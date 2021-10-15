#!/usr/bin/env python3

import os
import subprocess
import shutil
import sys


def find_files(d, find_args=None):
    if not find_args:
        find_args = []
    command = ['find', '-L', d,
               '-type', 'f']
    command.extend(find_args)
    ret = subprocess.run(command, stdout=subprocess.PIPE).stdout.splitlines()
    return [str(f, 'utf-8') for f in ret]


def find_java_files(src_dir):
    return find_files(src_dir, ['-name', '*.java'])


def directories(top_dir):
    command = ['find', top_dir,
               '-type', 'd']
    ret = subprocess.run(command, stdout=subprocess.PIPE).stdout.splitlines()
    return [str(d, 'utf-8') for d in ret]


def javadocs_command(src, javadoc, course_name):
    command = ['javadoc',
               '-sourcepath', src,
               '-d', javadoc,
               '-link', 'http://docs.oracle.com/javase/8/docs/api/',
               '-package',  # document everything except private members
               '-header', '<h1>{}</h1>'.format(course_name)]
    command.extend(find_java_files(src))
    return command


def print_cut_lines(filename, lines, line_number):
    print('CUT {}:{}:{}'.format(filename, line_number, line_number + len(lines) - 1))
    for line in lines:
        print('-' + line, end='')


def print_uncommented_lines(filename, line, line_number):
    print('UNCOMMENT {}:{}'.format(filename, line_number))
    print('+' + line, end='')

start_cut_symbol = '///*'
stop_cut_symbol = '//*/'
uncomment_symbol = '//**'
omit_file = '//**//**'


def strip_uncomment_symbol(line):
    return line.replace(uncomment_symbol, '') if uncomment_symbol in line else line


def copy_removing_code(src_file, dest_file):
    cutting = False
    omitting = False
    cut_lines = []
    line_number = None


    with open(src_file) as f, open(dest_file, 'w') as t:
        for number, line in enumerate(f, 1):
            if omit_file in line:
                omitting = True
                break
            if start_cut_symbol in line:
                cutting = True
                if not line_number:
                    line_number = number
            if not cutting:
                if uncomment_symbol in line:
                    line = line.replace(uncomment_symbol, '')
                    print_uncommented_lines(src_file, line, number)
                print(line, end='', file=t)
            else:
                cut_lines.append(line)
            if stop_cut_symbol in line:
                print_cut_lines(src_file, cut_lines, line_number)
                cutting = False
                cut_lines = []
                line_number = None
        if omitting:
            print("{} not included in starter/src".format(src_file))
            os.remove(dest_file)


def generate_starter_src(src, starter):
    src, starter = os.path.normpath(src), os.path.normpath(starter)

    def dest(src_path):
        return src_path.replace(src, starter, 1)
    
    for d in directories(src):
        os.makedirs(dest(d), exist_ok=True)
    for f in find_java_files(src):
        copy_removing_code(f, dest(f))


def open_permissions_recursively(d, file_filter_fn=None):
    if not os.path.isdir(d):
        return
    if not file_filter_fn:
        def file_filter_fn(f):
            return True
    for subd in directories(d):
        os.chmod(subd, 0o755)
    for f in find_files(d):
        if file_filter_fn(f):
            executable_bits = os.stat(f).st_mode & 0o111
            os.chmod(f, 0o644 | executable_bits)


def close_permissions_at_dir(dir):
    if os.path.isdir(dir):
        os.chmod(dir, 0o700)


def delete_directory(d):
    if os.path.isdir(d):
        shutil.rmtree(d)


def create_tarball(args):
    command = ["tar", "-c", "-X .gitignore", ".", "|", "bzip2", ">", args.filename]
    print('Running:', ' '.join(command))
    subprocess.call(' '.join(command), shell=True, stdout=sys.stdout, stderr=sys.stderr)


def create_starter_jar_name():
    return os.path.basename(os.getcwd()) + '-starter.jar'


def create_starter_jar(archive_dir, afilename, resources_dir, starter_dir):
    if not os.path.isdir(archive_dir):
        os.mkdir(archive_dir)

    command = ["jar", "-cvf", afilename]
    if os.path.isdir(resources_dir):
        command.append(resources_dir)
    command.extend(["-C", starter_dir, "."])

    print('Running:', ' '.join(command))
    subprocess.call(' '.join(command), shell=True, stdout=sys.stdout, stderr=sys.stderr)
    subprocess.call('mv {} {}'.format(afilename, archive_dir),
                    shell=True, stdout=sys.stdout, stderr=sys.stderr)


def generate_html_from_asciidoc(dir):
    for adoc in find_files(dir, ['-name', '*.adoc']):
        command = ['asciidoctor', adoc]
        print('Running: {}'.format(' '.join(command)))
        subprocess.run(command)
