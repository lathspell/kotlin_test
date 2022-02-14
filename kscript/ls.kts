#!/usr/bin/env kscript

import java.io.File

File("./").walk().forEach {
    println(it)
}

