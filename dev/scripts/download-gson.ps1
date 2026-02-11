$jarUrl = "https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.9/gson-2.8.9.jar"
$destDir = Join-Path $PSScriptRoot "..\lib"
if (-not (Test-Path $destDir)) { New-Item -ItemType Directory -Path $destDir | Out-Null }
$dest = Join-Path $destDir "gson-2.8.9.jar"
Write-Host "Downloading Gson from $jarUrl to $dest"
Invoke-WebRequest -Uri $jarUrl -OutFile $dest
Write-Host "Done."