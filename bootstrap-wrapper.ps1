$ErrorActionPreference = "Stop"
$uri = "https://raw.githubusercontent.com/NeoForgeMDKs/MDK-1.21.1-ModDevGradle/main/gradle/wrapper/gradle-wrapper.jar"
$dest = Join-Path $PSScriptRoot "gradle/wrapper/gradle-wrapper.jar"
New-Item -ItemType Directory -Force (Split-Path $dest) | Out-Null
Invoke-WebRequest -Uri $uri -OutFile $dest
Write-Host "Gradle wrapper installed: $dest"
