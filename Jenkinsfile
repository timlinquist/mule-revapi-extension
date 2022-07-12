def UPSTREAM_PROJECTS_LIST = [ "Mule-runtime/api-annotations/1.2.0-JULY-2022-WITH-FIXES-W-11403067-W-11147961" ]

Map pipelineParams = [ "upstreamProjects" : UPSTREAM_PROJECTS_LIST.join(','),
                       "mavenSettingsXmlId" : "mule-runtime-maven-settings-MuleSettings",
                       "projectType" : "Runtime"]

runtimeBuild(pipelineParams)
