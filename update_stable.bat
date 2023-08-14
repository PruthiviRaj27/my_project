@echo off

REM Delete the old tag locally
git tag -d stable

REM Delete the old tag remotely
git push origin :refs/tags/stable

REM Get the latest commit ID from the 'master' branch
for /f %%i in ('git rev-parse master') do set "commitId=%%i"

REM Make a new tag locally
git tag stable %commitId%

REM Push the new tag to the remote repository
git push origin stable

echo Tagging process completed.