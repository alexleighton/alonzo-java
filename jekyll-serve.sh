#!/usr/bin/expect

set timeout 5
spawn jekyll serve
expect {
    -re {Server address:\s+(.+)\r\n$} {
        set address $expect_out(1,string)
    }
}
expect "Server running..."
catch {
    exec open $address
}
interact
