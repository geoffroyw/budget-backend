class TransactionSerializer < ActiveModel::Serializer
  attributes :id, :name, :date, :amount, :transaction_type
  belongs_to :bank_account
end
