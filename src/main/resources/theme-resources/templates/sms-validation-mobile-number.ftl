<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "title">
        ${msg("updatePhoneNumberTitle", realm.name)}
    <#elseif section = "header">
        ${msg("updatePhoneNumberTitle", realm.name)}
    <#elseif section = "form">
        <#--
            Hack-alert: Keycloak doesn't provide per-field error messages here,
            so we check global message for need to display validation error styling
        -->
        <#if message?has_content && message.type = "error">
            <#assign errorClass = "govuk-form-group--error" >
        </#if>
        <div class="govuk-grid-row">
            <div class="govuk-grid-column-full">
                <p>${msg("updatePhoneNumberMessage")}</p>
            </div>
            <form id="kc-totp-login-form" class="${properties.kcFormClass!} govuk-grid-column-two-thirds" action="${url.loginAction}" method="post">
                <div class="govuk-form-group ${errorClass!""}">
                    <label for="mobileNumber" class="govuk-label">${msg("phoneNumber")}</label>
                    <input type="tel" id="mobileNumber" class="govuk-input" name="mobile_number" value="${(phoneNumber!'')}" autocomplete="mobile tel" aria-describedby="mobileNumber-hint" />
                </div>

                <div class="govuk-form-group">
                    <input class="govuk-button" type="submit" value="${msg("doSubmit")}"/>
                </div>
            </form>
        </div>
    </#if>
</@layout.registrationLayout>
